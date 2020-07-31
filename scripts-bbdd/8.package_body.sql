create or replace 
PACKAGE BODY                 "PCK_COLABORATIVO_MAPA" AS

  PROCEDURE GET(v_filas IN number,
    v_inicio IN number,
    v_orden in varchar2,
    v_ids in varchar2 default null,
    v_title in varchar2 default null,
    v_type in varchar2 default null,
    v_category in varchar2 default null,
    v_account in varchar2 default null,
    v_start_date in date default null,
    v_end_date in date default null,


   xmlsal OUT clob, total OUT number)
  IS
   l_usuarioalta varchar2(300);
   l_xmltype XMLTYPE;
   l_ctx dbms_xmlgen.ctxhandle;
   norows EXCEPTION;
   PRAGMA EXCEPTION_INIT(norows, -30625);
   fromwhere varchar2(1000);
  BEGIN
    l_usuarioalta := obtenerusuario(v_account);
    fromwhere := 'FROM POI_AGRUPACION
      WHERE 1=1 '|| generarWhere(v_ids,v_title,v_type,v_category,l_usuarioalta,v_start_date,v_end_date);
    total := GET_ROWS(fromWhere);
    if total > 0 then

     if v_orden is not null then
      fromWhere := fromWhere || ' order by ' || v_orden;
     end if;

     l_ctx := dbms_xmlgen.newcontext('SELECT id_agrupacion as id,
        nombre as title,
        icon as icon,
        CASE
          WHEN gcz_publicado = ''S'' THEN ''public''
          WHEN gcz_publicado = ''N'' THEN ''private''
          WHEN gcz_publicado = ''P'' THEN ''collaborative''
        END as type,

        CASE gcz_usuarioalta
             WHEN '''|| l_usuarioalta ||''' THEN  ''true''
             ELSE ''false''
        END as owned,

        to_char(nvl(gcz_fechamod,gcz_fechaalta),''YYYY-MM-DD"T"HH24:MI:SS"Z"'') as lastupdated
        ' || fromwhere);

    dbms_xmlgen.setMaxRows(l_ctx,v_filas);
    dbms_xmlgen.setskiprows(l_ctx, v_inicio);
    dbms_xmlgen.setrowsettag(l_ctx, 'resultado');
    dbms_xmlgen.setrowtag (l_ctx, 'result');
    l_xmltype := dbms_xmlgen.getxmltype(l_ctx);
    l_xmltype := xmltype(replace(l_xmltype.getClobVal(), '<result>', '<result xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="mapaColaborativo">'));
    dbms_xmlgen.closecontext (l_ctx);
    xmlsal := l_xmltype.getClobVal;
   else
     xmlsal :='<resultado/>';
   end if;
  EXCEPTION
    when norows then
      xmlsal :='<resultado/>';

  END GET;


PROCEDURE CATEGORIAS(xmlsal OUT clob, total OUT number)
IS
  
   l_xmltype XMLTYPE;
   l_ctx dbms_xmlgen.ctxhandle;
   norows EXCEPTION;
   PRAGMA EXCEPTION_INIT(norows, -30625);
   fromwhere varchar2(1000);
  BEGIN
  
     l_ctx := dbms_xmlgen.newcontext('select d.id,d.valor as title,d.tipo as type,l.numero as count from 
poi_categoria d,
(select c.id as id, count(*) as numero from poi_agrupacion_category a,poi_categoria c where a.id_categoria = c.id and a.id_agrupacion in (select id_agrupacion from poi_agrupacion where gcz_publicado in (''S'',''P'')) group by c.id) l
where l.id(+)=d.id order by 2 asc');

    dbms_xmlgen.setrowsettag(l_ctx, 'resultado');
    dbms_xmlgen.setrowtag (l_ctx, 'result');
    l_xmltype := dbms_xmlgen.getxmltype(l_ctx);
    l_xmltype := xmltype(replace(l_xmltype.getClobVal(), '<result>', '<result xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="category">'));
    dbms_xmlgen.closecontext (l_ctx);
    xmlsal := l_xmltype.getClobVal;
   
  EXCEPTION
    when norows then
      xmlsal :='<resultado/>';
END CATEGORIAS;


/*
<mapa>
   <title>Columpios</title>
   <type>private</type>
   <pois>
      <poi>
         <id>670</id>
         <title>mi titulo</title>
         <description>mi descripcion entera</description>
         <geometry>
            <type>Point</type>
            <coordinates>672550.9948366949,4613469.045065012</coordinates>
         </geometry>
      </poi>
      <poi>
         <id>671</id>
         <title>dsfgsdfg</title>
         <description>sdfgsdfg</description>
         <geometry>
            <type>Point</type>
            <coordinates>672480.3112866713,4613485.172059045</coordinates>
         </geometry>
      </poi>
   </pois>
</mapa>
*/
  PROCEDURE CREAR(
        v_account varchar,
        xmlmapa CLOB,

        xmlsal OUT CLOB
    )
  IS
   l_usuario varchar2(300);
   v_id_agrupacion number;
   v_title varchar2(500);
   v_tipo varchar2(500);
   v_icon varchar2(500);
   v_tipo_interno varchar2(1);
   v_UsuarioNoExistente EXCEPTION;
  BEGIN
    l_usuario := OBTENERUSUARIO(v_account);

    select max(id_agrupacion) + 1 into v_id_agrupacion from poi_agrupacion;

    select  extractvalue(T.COLUMN_VALUE, '/mapa/title') as title,
          extractvalue(T.COLUMN_VALUE, '/mapa/type') as tipo,
          extractvalue(T.COLUMN_VALUE, '/mapa/icon') as icon
    into v_title,v_tipo,v_icon from table(xmlsequence(extract(xmltype(xmlmapa), '/mapa'))) t;

    if v_tipo = 'public' then
      v_tipo_interno := 'S';
    elsif v_tipo = 'collaborative' then
      v_tipo_interno := 'P';
    else
      v_tipo_interno := 'N';
    end if;

    

    insert into poi_agrupacion (ID_AGRUPACION, NOMBRE, GCZ_PUBLICADO, GCZ_FECHAPUB, GCZ_USUARIOPUB, GCZ_USUARIOALTA, TIPO, ICON)
              values (v_id_agrupacion,v_title,v_tipo_interno,sysdate,l_usuario,l_usuario,99/*tipo*/,v_icon);
              
    FOR r IN (select  extractvalue(T.COLUMN_VALUE, '/category/id') as id
              from table(xmlsequence(extract(xmltype(xmlmapa), '/mapa/categories/category'))) t)
    LOOP
      insert into POI_AGRUPACION_CATEGORY(id_agrupacion,id_categoria) values (v_id_agrupacion, r.id);
    END LOOP;
              
    FOR r IN (select  extractvalue(T.COLUMN_VALUE, '/poi/id') as id,
                      extractvalue(T.COLUMN_VALUE, '/poi/title') as title,
                      extractvalue(T.COLUMN_VALUE, '/poi/link') as link,
                      extractvalue(T.COLUMN_VALUE, '/poi/description') as description,
                      extractvalue(T.COLUMN_VALUE, '/poi/style/styles/fillColor') as fill,
                      extractvalue(T.COLUMN_VALUE, '/poi/style/styles/fillOpacity') as fillOpacity,
                      extractvalue(T.COLUMN_VALUE, '/poi/style/styles/strokeColor') as stroke,
                      extractvalue(T.COLUMN_VALUE, '/poi/style/styles/strokeOpacity') as strokeOpacity,
                      extractvalue(T.COLUMN_VALUE, '/poi/style/styles/strokeWidth') as strokeWidth,
                      extractvalue(T.COLUMN_VALUE, '/poi/icon') as icon,
                      extractvalue(T.COLUMN_VALUE, '/poi/geometry/type') as tipo,
                      extractvalue(T.COLUMN_VALUE, '/poi/geometry/coordinates') as coordinates
              from table(xmlsequence(extract(xmltype(xmlmapa), '/mapa/pois/poi'))) t)
    LOOP
      if r.id is null then
        select SEQ_POI_INFO.NEXTVAL into r.id FROM DUAL;
      end if;

      insert into poi_info (ID_POI,ID_AGRUPACION,GCZ_PUBLICADO,GCZ_USUARIOPUB,GCZ_USUARIOALTA) values(r.id,v_id_agrupacion,'S',l_usuario,l_usuario);
      if r.title is not null then
        insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,1,r.title,v_id_agrupacion);
      end if;
      if r.description is not null then
        insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,2,r.description,v_id_agrupacion);
      end if;
      
      if r.fill is not null then
        insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,7,r.fill,v_id_agrupacion);
      end if;
      if r.fillOpacity is not null then
        insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,8,r.fillOpacity,v_id_agrupacion);
      end if;
      if r.stroke is not null then
        insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,9,r.stroke,v_id_agrupacion);
      end if;
      if r.strokeOpacity is not null then
        insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,10,r.strokeOpacity,v_id_agrupacion);
      end if;
      if r.strokeWidth is not null then
        insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,11,r.strokeWidth,v_id_agrupacion);
      end if;
      if r.icon is not null then
        insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,12,r.icon,v_id_agrupacion);
      end if;
      
      if r.link is not null then
        insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,4,r.link,v_id_agrupacion);
      end if;
      if r.coordinates is not null then
        if r.tipo='LineString' then
          insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,5,r.coordinates,v_id_agrupacion);
        elsif r.tipo='Polygon' then
          insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,6,r.coordinates,v_id_agrupacion);
        else
          insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,3,r.coordinates,v_id_agrupacion);
        end if;
      end if;

    END LOOP;
    commit;
    DETALLE(v_id_agrupacion,v_account, null,xmlsal);
  EXCEPTION

    WHEN v_UsuarioNoExistente THEN
          RAISE_APPLICATION_ERROR(-20002,'Usuario no existente');
          ROLLBACK;
    WHEN OTHERS THEN
          raise_application_error(-20001,'An error was encountered - '||SQLCODE||' -ERROR- '||SQLERRM);
          ROLLBACK;
  END CREAR;

  /*
<mapa>
   <id>36</id>
   <title>Columpios</title>
   <type>private</type>
   <pois>
      <poi>
         <id>670</id>
         <title>mi titulo</title>
         <description>mi descripcion entera</description>
         <geometry>
            <type>Point</type>
            <coordinates>672550.9948366949,4613469.045065012</coordinates>
         </geometry>
      </poi>
      <poi>
         <id>671</id>
         <title>dsfgsdfg</title>
         <description>sdfgsdfg</description>
         <geometry>
            <type>Point</type>
            <coordinates>672480.3112866713,4613485.172059045</coordinates>
         </geometry>
      </poi>
   </pois>
</mapa>
*/
  PROCEDURE GUARDAR(
        v_account varchar,
        xmlmapa CLOB,

        xmlsal OUT CLOB
    )
  IS
   l_usuario varchar2(300);
   v_id_agrupacion number;
   v_title varchar2(500);
   v_link varchar2(500);
   v_tipo varchar2(500);
   v_icon varchar2(500);
   v_tipo_interno varchar2(1);
   v_UsuarioNoExistente EXCEPTION;
  BEGIN
    l_usuario := OBTENERUSUARIO(v_account);


    -- TODO a¿r tipo (publico, privado, colaborativo)
    select
          extractvalue(T.COLUMN_VALUE, '/mapa/id') as title,
          extractvalue(T.COLUMN_VALUE, '/mapa/title') as title,
          extractvalue(T.COLUMN_VALUE, '/mapa/link') as link,
          extractvalue(T.COLUMN_VALUE, '/mapa/type') as tipo,
          extractvalue(T.COLUMN_VALUE, '/mapa/icon') as icon
    into v_id_agrupacion,v_title,v_link,v_tipo,v_icon from table(xmlsequence(extract(xmltype(xmlmapa), '/mapa'))) t;

    if v_tipo = 'public' then
      v_tipo_interno := 'S';
    elsif v_tipo = 'collaborative' then
      v_tipo_interno := 'P';
    else
      v_tipo_interno := 'N';
    end if;
    
    update poi_agrupacion set nombre=v_title,gcz_publicado=v_tipo_interno,icon=v_icon,gcz_fechamod=sysdate
    where id_agrupacion=v_id_agrupacion and gcz_usuarioalta=l_usuario;

    delete from POI_AGRUPACION_CATEGORY where id_agrupacion=v_id_agrupacion;
    FOR r IN (select  extractvalue(T.COLUMN_VALUE, '/category/id') as id
              from table(xmlsequence(extract(xmltype(xmlmapa), '/mapa/categories/category'))) t)
    LOOP
      insert into POI_AGRUPACION_CATEGORY(id_agrupacion,id_categoria) values (v_id_agrupacion, r.id);
    END LOOP;

   delete from poi_info where id_agrupacion=v_id_agrupacion and gcz_usuarioalta=l_usuario;
    FOR r IN (select  extractvalue(T.COLUMN_VALUE, '/poi/id') as id,
                      extractvalue(T.COLUMN_VALUE, '/poi/title') as title,
                      extractvalue(T.COLUMN_VALUE, '/poi/link') as link,
                      extractvalue(T.COLUMN_VALUE, '/poi/description') as description,
                      extractvalue(T.COLUMN_VALUE, '/poi/geometry/type') as tipo,
                      extractvalue(T.COLUMN_VALUE, '/poi/style/styles/fillColor') as fill,
                      extractvalue(T.COLUMN_VALUE, '/poi/style/styles/fillOpacity') as fillOpacity,
                      extractvalue(T.COLUMN_VALUE, '/poi/style/styles/strokeColor') as stroke,
                      extractvalue(T.COLUMN_VALUE, '/poi/style/styles/strokeOpacity') as strokeOpacity,
                      extractvalue(T.COLUMN_VALUE, '/poi/style/styles/strokeWidth') as strokeWidth,
                      extractvalue(T.COLUMN_VALUE, '/poi/icon') as icon,
                      extractvalue(T.COLUMN_VALUE, '/poi/updateable') as updateable,
                      extractvalue(T.COLUMN_VALUE, '/poi/geometry/coordinates') as coordinates
              from table(xmlsequence(extract(xmltype(xmlmapa), '/mapa/pois/poi'))) t)
    LOOP
     -- if r.updateable = 'true' then
        if r.id is null then
          select SEQ_POI_INFO.NEXTVAL into r.id FROM DUAL;
        --else
          --delete from poi_info where id_poi=r.id and id_agrupacion=v_id_agrupacion and gcz_usuarioalta=l_usuario;
        end if;
        insert into poi_info (ID_POI,ID_AGRUPACION,GCZ_PUBLICADO,GCZ_USUARIOPUB,GCZ_USUARIOALTA) values(r.id,v_id_agrupacion,'S',l_usuario,l_usuario);
        if r.title is not null then
          insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,1,r.title,v_id_agrupacion);
        end if;
        if r.description is not null then
          insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,2,r.description,v_id_agrupacion);
        end if;
        
        if r.fill is not null then
          insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,7,r.fill,v_id_agrupacion);
        end if;
        if r.fillOpacity is not null then
          insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,8,r.fillOpacity,v_id_agrupacion);
        end if;
        if r.stroke is not null then
          insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,9,r.stroke,v_id_agrupacion);
        end if;
        if r.strokeOpacity is not null then
          insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,10,r.strokeOpacity,v_id_agrupacion);
        end if;
        if r.strokeWidth is not null then
          insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,11,r.strokeWidth,v_id_agrupacion);
        end if;
        if r.icon is not null then
          insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,12,r.icon,v_id_agrupacion);
        end if;
        
        if r.link is not null then
          insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,4,r.link,v_id_agrupacion);
        end if;
        if r.coordinates is not null then
          if r.tipo='LineString' then
            insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,5,r.coordinates,v_id_agrupacion);
          elsif r.tipo='Polygon' then
            insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,6,r.coordinates,v_id_agrupacion);
          else
            insert into poi_valor_etiqueta(ID_POI,ID_ETIQUETA,VALOR,ID_AGRUPACION) values (r.id,3,r.coordinates,v_id_agrupacion);
          end if;
        end if;
      --  end if;
    END LOOP;
    commit;
    DETALLE(v_id_agrupacion,v_account, null,xmlsal);
  EXCEPTION

    WHEN v_UsuarioNoExistente THEN
          RAISE_APPLICATION_ERROR(-20002,'Usuario no existente');
          ROLLBACK;
    WHEN OTHERS THEN
          raise_application_error(-20001,'An error was encountered - '||SQLCODE||' -ERROR- '||SQLERRM);
          ROLLBACK;
  END GUARDAR;

  FUNCTION generarWhere(v_ids in varchar2 default null,
    v_title in varchar2 default null,
    v_type in varchar2 default null,
    v_category in varchar2 default null,
    v_account in varchar2 default null,
    v_start_date in date default null,
    v_end_date in date default null) RETURN string
  IS
  consulta varchar2(4000);
  BEGIN

    if v_ids is not null then
      consulta := consulta || ' AND ID_AGRUPACION in (' || v_ids || ')';
    end if;
    if v_category is not null then
      consulta := consulta || ' AND ID_AGRUPACION in (select ID_AGRUPACION from POI_AGRUPACION_CATEGORY where id_categoria in (' || v_category || '))';
    end if;
    -- TODO busqueda por varias palabras
    if v_title is not null then
      consulta := consulta || ' AND lower(nombre) like lower(''%'|| v_title ||'%'') ';
    end if;
    if v_type is not null then
      if v_type='collaborative' then
        consulta := consulta || ' AND gcz_publicado=''P'' ';
      else
        consulta := consulta || ' AND gcz_publicado=''S'' ';
      end if;
    end if;
    if v_account is not null then
      consulta := consulta || ' AND (lower(GCZ_USUARIOALTA) = lower('''|| v_account ||''') or (id_agrupacion in (select distinct(id_agrupacion) from poi_info where gcz_usuarioalta=''' || v_account || ''') and gcz_publicado=''P''))';
    else
      consulta := consulta || ' AND (gcz_publicado = ''S'' or gcz_publicado=''P'')';
    end if;
    if v_start_date is not null then
      consulta := consulta || ' AND GCZ_FECHAALTA between to_date(''' || to_char(v_start_date,'dd/mm/yyyy') || ''',''dd/mm/yyyy'') and to_date(''' || to_char(v_end_date,'dd/mm/yyyy') || ''',''dd/mm/yyyy'') ';
    end if;
    return consulta;
  END;

  PROCEDURE DETALLE(
    v_id in numeric,
    v_account in varchar2,
    v_icono in varchar2 default null,
    xmlsal OUT CLOB)

   IS
   l_usuario varchar2(300);
   l_xmltype XMLTYPE;
   norows EXCEPTION;
   PRAGMA EXCEPTION_INIT(norows, -30625);
   BEGIN
   null;
     -- TODO si el usuario se indica comprobar que es el que lo ha creado
     -- TODO si el usuario no se indica mostrar solo los publicos o colaborativos
     l_usuario := OBTENERUSUARIO(v_account);
     select xmlelement("mapa",
                    xmlelement("id", m.id_agrupacion),
                    xmlelement("owned", (CASE m.gcz_usuarioalta
                                                      WHEN l_usuario THEN  'true'
                                                      ELSE 'false'
                                                      END)),
                    xmlelement("type", CASE
                        WHEN gcz_publicado = 'S' THEN 'public'
                        WHEN gcz_publicado = 'N' THEN 'private'
                        WHEN gcz_publicado = 'P' THEN 'collaborative'
                      END),
                    xmlelement("title", m.nombre),
                    xmlelement("icon", m.icon),

                    xmlelement("lastupdated", to_char(nvl(m.gcz_fechamod,m.gcz_fechaalta),'YYYY-MM-DD"T"HH24:MI:SS"Z"')),
                    
                     (SELECT xmlagg(xmlelement("categories",
                                                   xmlelement("id", c.id),
                                                   xmlelement("title", c.valor)
                                                 )
                                      )
                                FROM POI_CATEGORIA c, POI_AGRUPACION_CATEGORY a
                                WHERE c.id = a.id_categoria and a.id_agrupacion = m.id_agrupacion

                         ),
                    
                    
                        (SELECT xmlagg(xmlelement("pois",
                                                   xmlelement("id", i.id_poi),
                                                   xmlelement("title", (select valor from poi_valor_etiqueta where id_agrupacion=v_id and id_poi=i.id_poi and id_etiqueta=1)),
                                                   xmlelement("link", (select valor from poi_valor_etiqueta where id_agrupacion=v_id and id_poi=i.id_poi and id_etiqueta=4)),
                                                   xmlelement("category", (select valor from poi_valor_etiqueta where id_agrupacion=v_id and id_poi=i.id_poi and id_etiqueta=13)),
                                                   xmlelement("updateable", (CASE i.gcz_usuarioalta
                                                      WHEN l_usuario THEN  'true'
                                                      ELSE 'false'
                                                      END)),
                                                   xmlelement("description", (select valor from poi_valor_etiqueta where id_agrupacion=v_id and id_poi=i.id_poi and id_etiqueta=2)),
                                                   xmlelement("icon", (select valor from poi_valor_etiqueta where id_agrupacion=v_id and id_poi=i.id_poi and id_etiqueta=12)),
                                                   xmlelement("style",
                                                      xmlelement("fillColor", (select valor from poi_valor_etiqueta where id_agrupacion=v_id and id_poi=i.id_poi and id_etiqueta=7)),
                                                      xmlelement("fillOpacity", (select valor from poi_valor_etiqueta where id_agrupacion=v_id and id_poi=i.id_poi and id_etiqueta=8)),
                                                      xmlelement("strokeColor", (select valor from poi_valor_etiqueta where id_agrupacion=v_id and id_poi=i.id_poi and id_etiqueta=9)),
                                                      xmlelement("strokeOpacity", (select valor from poi_valor_etiqueta where id_agrupacion=v_id and id_poi=i.id_poi and id_etiqueta=10)),
                                                      xmlelement("strokeWidth", (select valor from poi_valor_etiqueta where id_agrupacion=v_id and id_poi=i.id_poi and id_etiqueta=11))
                                                   ),
                                                   xmlelement("icongeneral", 'http://www.zaragoza.es/contenidos/iconos/' || nvl(v_icono,'generico') ||'.png'),
                                                   
                                                   xmlelement("coordenadas", (select valor||'#'||id_etiqueta from poi_valor_etiqueta where id_agrupacion=v_id and id_poi=i.id_poi and id_etiqueta in (3,5,6)))
                                                   
                                                   /*comentarios*/
                                                   ,(
                                                    select xmlagg(xmlelement("comments",
                                                              xmlelement("description",c.description),
                                                              xmlelement("screen_name",nvl(usr.screenname,usr.person_name)),
                                                              xmlelement("icon",nvl2(usr.image, 'https://www.zaragoza.es/cont/paginas/zona-personal/' || usr.image, null))
                                                              )
                                                    )
                                                              from poi_comment c, general.users usr
                                                              where c.id_agrupacion=m.id_agrupacion and c.id_poi=i.id_poi and c.id_usuario_adentra=usr.id
                                                   )
                                                   /*fin comentarios*/
                                                   
                                                   
                                                   
                                                 )
                                      )
                                FROM poi_info i
                                WHERE i.id_agrupacion = m.id_agrupacion

                         )

      ) into l_xmltype
      from poi_agrupacion m
      where m.id_agrupacion=v_id;
   xmlsal := l_xmltype.getClobVal;
  EXCEPTION
    when norows then
      xmlsal :='<resultado/>';

  END DETALLE;


  procedure ELIMINAR(v_account IN varchar, v_id in numeric, v_ids in varchar2, registros OUT numeric)
  IS
    l_usuario varchar2(300);
  BEGIN
    l_usuario := OBTENERUSUARIO(v_account);

    if v_ids is null then
    -- Eliminamos el mapa si el usuario es el propietario...
      delete from poi_agrupacion where id_agrupacion=v_id and gcz_usuarioalta=l_usuario;
      registros := SQL%ROWCOUNT;
    else
      -- Eliminamos los puntos que el usuario ha creado

      delete from poi_info where id_agrupacion=v_id
        and id_poi in (select   regexp_substr(v_ids,'[^,]+', 1, level) from dual
            connect by regexp_substr(v_ids, '[^,]+', 1, level) is not null)
        and (gcz_usuarioalta=l_usuario);

      registros := SQL%ROWCOUNT;
      -- TODO permitir que el propietario del mapa pueda borrar cualquier punto
    end if;

  --EXCEPTION

  END ELIMINAR;

  FUNCTION GET_ROWS(pString IN varchar2) return integer
    IS
    -----------------------------------------------------------------
    -- DECLARACIONES
    -----------------------------------------------------------------
    cursor_name INTEGER;
    v_total number;
    reg integer;
  BEGIN
    execute immediate 'select count(1) ' || pString into v_total;
    return v_total;
    EXCEPTION
      WHEN OTHERS THEN
        IF DBMS_SQL.IS_OPEN(cursor_name) THEN
          DBMS_SQL.CLOSE_CURSOR(cursor_name);
        END IF;
        RAISE;
  END GET_ROWS;


  FUNCTION OBTENERUSUARIO(pString IN varchar2) return varchar
    is
    usuario varchar(300);
    BEGIN
    select email into usuario from general.users where guid=pString;
    return usuario;
    EXCEPTION
      when NO_DATA_FOUND
        then return null;
  END OBTENERUSUARIO;


END PCK_COLABORATIVO_MAPA;