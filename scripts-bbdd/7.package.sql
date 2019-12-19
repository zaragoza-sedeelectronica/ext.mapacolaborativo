create or replace 
PACKAGE                 "PCK_COLABORATIVO_MAPA" AS TYPE cursorRetorno IS REF CURSOR;
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


   xmlsal OUT clob, total OUT number);
   
   PROCEDURE CATEGORIAS(xmlsal OUT clob, total OUT number);
   
    PROCEDURE DETALLE(v_id in numeric, v_account in varchar2,v_icono in varchar2 default null,xmlsal OUT CLOB);

    PROCEDURE CREAR(
        v_account varchar,
				xmlmapa CLOB,

        xmlsal OUT CLOB
    );
    PROCEDURE GUARDAR(
        v_account varchar,
				xmlmapa CLOB,

        xmlsal OUT CLOB
    );
    procedure ELIMINAR(v_account IN varchar, v_id in numeric, v_ids in varchar2, registros OUT numeric);

    FUNCTION generarWhere(v_ids in varchar2 default null,
      v_title in varchar2 default null,
      v_type in varchar2 default null,
      v_category in varchar2 default null,
      v_account in varchar2 default null,
      v_start_date in date default null,
      v_end_date in date default null) RETURN string;

    FUNCTION GET_ROWS(pString IN varchar2) return integer;
    FUNCTION OBTENERUSUARIO(pString IN varchar2) return varchar;
END PCK_COLABORATIVO_MAPA;