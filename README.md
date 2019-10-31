### OpenCity-Ext - Mapas Colaborativos ###

# Introducción

Los Mapas Colaborativos son un instrumento de participación, colaboración y co-creación que facilita a los diferentes agentes (empresas, organizaciones ciudadanas, emprendedores, personas, etc.) acceder y utilizar los datos abiertos espaciales de Zaragoza para conocer su barrio y realizar propuestas para su mejora.

Toda la información sobre este módulo está en el [siguiente enlace](documentacion/). Aquí puede consultar:

* [el documento funcional](documentacion/funcional/README.md) 
* [el manual de uso](documentacion/manual/README.md).

A continuación se indican las instrucciones necesarias para la descarga y configuración del módulo de "Mapas Colaborativos" del proyecto OpenCity-Ext desarrollado por el Ayuntamiento de Zaragoza.

## Manual de instalación ##

Primero, hay que realizar los siguientes pasos del manual general:

* instalar y comprobar versiones software
* configurar la base de datos
* clonar repositorios:
    * mapas colaborativos
    * assets
    * fragmentos
    * servicio
    * portal
    * i18n (internalización)
* instalación de librerías en repositorio local
* configuración servidor
* configuración del módulo
* prueba del módulo
* otras configuraciones

# Instalar y comprobar versiones software

Las versiones mínimas del software son:

* Java 1.8
* Maven >= 3.0.5
* Oracle 11
* Eclipse 2019-03 (recomendada)

Una vez instalado Eclipse, hay que instalar  maven integration for eclipse desde la siguiente url http://download.eclipse.org/releases/indigo/ y seleccionando la opción "Generarl Purpose -> m2e"

# Configurar la base de datos

El sistema de mapas colaborativos utiliza dos usuarios diferentes para:

* noticias: de donde toma los datos de usuarios
* participacion: de donde toma los datos de los mapas colaborativos

Por lo tanto, hay que configurar las dos bases de datos para que el modulo funcione correctamente. Para eso hay que realizar los siguientes pasos:
* generar los usuarios en Oracle
* ejecutar los scripts de generación de las bases de datos
* configurar las bases de datos en el proyecto sede

A continuación se detallan los pasos a seguir en cada uno de ellos.

## Generar los usuarios en Oracle

Como se indica en el apartado anterior, hay que generar dos usuarios para noticias y participacion. Los usuarios se deben generar en Oracle ya que el sistema de mapas colaborativos utiliza Oracle.

Se aconseja que los nombres de los usuarios sean "noticias" y "participacion" para una mayor claridad en el uso de los mismos. Además, ambos usuarios deben poder acceder al contenido del otro. A continuación se muestran los comandos que deben utilizarse en la consola de SQL*Plus, si se utiliza dicha consola para realizar este paso.

Para generar un usuario, se utilizarían los comandos:
```
CREATE USER noticias IDENTIFIED BY "password";
CREATE USER participacion IDENTIFIED BY "password";
```

Para otorgar privilegios a los usuarios, se utilizarían los comandos:
```
GRANT ALL PRIVILEGES TO noticias;
GRANT ALL PRIVILEGES TO participacion;
```

Con estos pasos, ya estarías los usuarios y los privilegios necesarios para ambos.

## Ejecutar los scripts de generación de las bases de datos

El siguiente paso es crear las tablas asociadas a cada usuario y cargar los datos de prueba disponibles en cada uno de ellos. Para cada uno de los usuarios, hay una serie de scripts disponibles:

* noticias: los ficheros a usar están en [scripts-bbdd/noticias/](scripts-bbdd/noticias/)
* participacion: los ficheros están disponibles en [scripts-bbdd/participacion/](scripts-bbdd/participacion/)

El orden de ejecución es el siguiente:

* noticias:
    * db.sql
	* users_data.sql
    * users_db.sql
* participacion:
	* db.sql
	* package.sql
	* package_body.sql
	* data.sql
	
Una vez realizados estos pasos, ya estaría lista la base de datos en Oracle. 

# Clonar repositorios:

A continuación se listan los repositorios a clonar: 

* mapas colaborativos: https://bitbucket.org/masilgado/mapas-colaborativos.git
* assets: Es un repositorio que almacena componentes (css, javascript) que se reutiliza en diferentes portales o servicios.
**Clonar el repositorio:**
```
	https://bitbucket.org/zaragoza/sede.assets.git
```

* fragmentos: Es un repositorio que almacena código que se reutiliza en diferentes portales o servicios.  
**Clonar el repositorio:**
```
	https://<usuario>@bitbucket.org/zaragoza/sede.fragmentos.git
```

* servicio: En este repositorio se almacenan las plantillas que se generan desde cada uno de los Controllers existentes en sede.  
**Clonar el repositorio:**
```
	https://<usuario>@bitbucket.org/zaragoza/sede.servicio.git
```

* portal: Crear manualmente el directorio `portal` al mismo nivel que `fragmentos` y `servicio`
Repositorio que almacena los portales y/o páginas estáticas. Permite establecer subdirectorios para indicar portales secundarios recogidos bajo un portal principal.  
**Clonar el repositorio:**
```
	https://<usuario>@bitbucket.org/zaragoza/sede.portal.git
```

* i18n (internalización)
En `bean-I18n.xml` se define la configuración de localización de los ficheros de cada idioma, se debe crear un fichero por idioma, por ejemplo, `messages_es.properties` para castellano.
```
	prueba=texto en castellano
	prueba2=otro texto en castellano
```
El contenido de estos ficheros se actualiza según el parámetro `cacheSeconds` del fichero `bean-I18n.xml`.  
**Clonar el repositorio:**
```
	https://<usuario>@bitbucket.org/zaragoza/sede.i18n.git
```

# Instalación de librerías en repositorio local:

Existen librerías que no están disponibles en repositorios maven, son las que se encuentran en la carpeta [librerias](librerias/) y se deben instalar en el repositorio maven de local ejecutando lo siguiente.

```
$ mvn install:install-file -DgroupId=org.zaragoza -DartifactId=opencity.ext.core -Dversion=0.0.1 -Dpackaging=jar -Dfile=<path>/opencity.ext.core-0.0.1.jar -DgeneratePom=true

$ mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc5 -Dversion=11.2.0 -Dpackaging=jar -Dfile=<path>/ojdbc5.jar -DgeneratePom=true

$ mvn install:install-file -DgroupId=virtuoso.jena.driver -DartifactId=virtjdbc -Dversion=3 -Dpackaging=jar -Dfile=<path>/virtjdbc.jar -DgeneratePom=true

$ mvn install:install-file -DgroupId=virtuoso.jena -DartifactId=virt_jena -Dversion=3 -Dpackaging=jar -Dfile=<path>/virt_jena.jar -DgeneratePom=true

$ mvn install:install-file -DgroupId=idezar -DartifactId=geoapi -Dversion=2.0 -Dpackaging=jar -Dfile=<path>/geoapi.jar -DgeneratePom=true

$ mvn install:install-file -DgroupId=idezar -DartifactId=geoapi-nogenerics -Dversion=2.1-M2 -Dpackaging=jar -Dfile=<path>/geoapi-nogenerics.jar -DgeneratePom=true

$ mvn install:install-file -DgroupId=idezar -DartifactId=deegree2-pre -Dversion=2 -Dpackaging=jar -Dfile=<path>/deegree2-pre.jar -DgeneratePom=true

$ mvn install:install-file -DgroupId=idezar -DartifactId=gsl-coordinate-transformation -Dversion=1.0-jdk15 -Dpackaging=jar -Dfile=<path>/gsl-coordinate-transformation.jar -DgeneratePom=true

$ mvn install:install-file -DgroupId=idezar -DartifactId=gt2-epsg-wkt -Dversion=2.3.5 -Dpackaging=jar -Dfile=<path>/gt2-epsg-wkt.jar -DgeneratePom=true

$ mvn install:install-file -DgroupId=idezar -DartifactId=gt2-referencing -Dversion=2.3.0 -Dpackaging=jar -Dfile=<path>/gt2-referencing.jar -DgeneratePom=true

$ mvn install:install-file -DgroupId=idezar -DartifactId=iaaa_csct -Dversion=1.6.2 -Dpackaging=jar -Dfile=<path>/iaaa_csct.jar -DgeneratePom=true

$ mvn install:install-file -DgroupId=idezar -DartifactId=j3dcore -Dversion=1.3.0 -Dpackaging=jar -Dfile=<path>/j3dcore.jar -DgeneratePom=true

$ mvn install:install-file -DgroupId=idezar -DartifactId=j3dutils -Dversion=1.3.0 -Dpackaging=jar -Dfile=<path>/j3dutils.jar -DgeneratePom=true

$ mvn install:install-file -DgroupId=idezar -DartifactId=jai_codec -Dversion=1.1.1_01 -Dpackaging=jar -Dfile=<path>/jai_codec.jar -DgeneratePom=true

$ mvn install:install-file -DgroupId=idezar -DartifactId=jai_core -Dversion=1.1.1_01 -Dpackaging=jar -Dfile=<path>/jai_core.jar -DgeneratePom=true

$ mvn install:install-file -DgroupId=idezar -DartifactId=jGridShiftApi -Dversion=2.0 -Dpackaging=jar -Dfile=<path>/jGridShiftApi.jar -DgeneratePom=true

$ mvn install:install-file -DgroupId=idezar -DartifactId=jsr108 -Dversion=0.01 -Dpackaging=jar -Dfile=<path>/jsr108.jar -DgeneratePom=true

$ mvn install:install-file -DgroupId=idezar -DartifactId=jts -Dversion=1.6 -Dpackaging=jar -Dfile=<path>/jts.jar -DgeneratePom=true

$ mvn install:install-file -DgroupId=idezar -DartifactId=vecmath -Dversion=1.3.1 -Dpackaging=jar -Dfile=<path>/vecmath.jar -DgeneratePom=true
```

# Configuración servidor

El contenido de los proyectos clonados deben estar dispuestos con la siguiente estructura de directorios, empezando todos con la carpeta cont:

```
cont
|--- assets (repositorio https://<usuario>@bitbucket.org/zaragoza/sede.assets.git)
cont/vistas
|--- fragmentos (repositorio https://<usuario>@bitbucket.org/zaragoza/sede.fragmentos.git)
|--- servicio (repositorio https://<usuario>@bitbucket.org/zaragoza/sede.servicio.git)
|--- portal
|---|----- datos-abiertos (repositorio https://<usuario>@bitbucket.org/zaragoza/sede.portal.datos-abiertos.git)
```

Una vez realizado este paso, hay que configurar un servidor apache para que pueda servir contenidos estáticos. Para ello, hay que incluir en su configuración un proxy inverso:

```
ProxyPass /sede http://localhost:8888/opencityext
ProxyPassReverse /sede http://localhost:8888/opencityext

ProxyPass /sede http://localhost:8888/cont
ProxyPassReverse /sede http://localhost:8888/cont
```

Si el contenido del directorio `cont` no se sirve mediante un servidor sino como un contenido estático, hay que habilitar los modulos `proxy` y `http_proxy` en el servidor Apache e incluir en el fichero de configuración:
```
Alias /cont /<path>/cont
<Directory  <path>/cont>
    Options Indexes FollowSymLinks
    AllowOverride None
    Require all granted
</Directory>
```

# Configuración del módulo

Lo primero sería realizar 

```
$ mvn clean install
```

dentro del proyecto `opencity.ext.mapas` 

Una vez realizad, hay que configurar los diferentes ficheros de configuración que se utilizan en el proyecto.

La ubicación de las vistas se define en `opencity.ext.web/src/main/resources/application.properties` (es necesario copiar `opencity.ext.web/src/main/resources/application.properties.template` como `opencity.ext.web/src/main/resources/application.properties` para modificarlo):

```
thymeleaf.view=<path-opencity.ext.web>/src/main/webapp/vistas/
path.i18n=<path-opencity.ext.web>/src/main/webapp/i18n/
datasource.prefix=java:/comp/env/
```

Esta definición se puede realizar para cada uno de los entornos en `resources`, `resources-dev`, `resources-prod` y `resources-test`.

Los datos de conexión a la BBDD se define en `opencity.ext.web/src/main/resources/META-INF/context.xml` (es necesario copiar `opencity.ext.web/src/main/resources/META-INF/context.xmltemplate` como `opencity.ext.web/src/main/resources/META-INF/context.xml` para modificarlo):

```
<Resource name="jdbc/WebParticipacionDS" auth="Container"
              type="javax.sql.DataSource" driverClassName="oracle.jdbc.OracleDriver"
              url="jdbc:oracle:thin:@localhost:1521/ORCL"
              username="USER" password="PASS" maxActive="20" maxIdle="10"
              maxWait="-1"/>
<Resource name="jdbc/WebNoticiasDS" auth="Container"
              type="javax.sql.DataSource" driverClassName="oracle.jdbc.OracleDriver"
              url="jdbc:oracle:thin:@localhost:1521/ORCL"
              username="USER" password="PASS" maxActive="20" maxIdle="10"
              maxWait="-1"/>
```

Hay que indicar los valores de los campos `url`, `user` y `password`.

# Prueba del módulo

Para lanzar el módulo, hay que ejecutar la siguiente instrucción:
```
$ mvn -Dmaven.tomcat.port=8888 tomcat7:run
```

Para probar que funciona correctamente acceder a:
```
http://localhost:8888/opencityext/servicio/mapa-colaborativo/
```

Puede ser que haya elementos que no se muestren correctamente por lo que se aconseja que mejor se utilice 
```
http://localhost/opencityext/servicio/mapa-colaborativo/
```

asegurándonos de que el servidor Apache está arrancado.

# Otras configuraciones

Con los pasos indicados anteriormente, ya podría trabajar con el módulo sin problemas. No obstante, existentes más configuraciones que pueden realizarse, las cuales se indican a continuación.

## Trabajar con proxy
Configurar maven en $HOME/.m2/ crear el fichero settings.xml
```
<settings>
  <proxies>
    <proxy>
      <id>http_proxy</id>
      <active>true</active>
      <protocol>http</protocol>
      <host>proxy.red.zaragoza.es</host>
      <port>8080</port>
    </proxy>
    <proxy>
      <id>https_proxy</id>
      <active>true</active>
      <protocol>https</protocol>
      <host>proxy.red.zaragoza.es</host>
      <port>8080</port>
    </proxy>
 </proxies>
</settings>
```
$ mvn -Dmaven.tomcat.port=8888 -Dhttp.proxyHost=<host> -Dhttp.proxyPort=<port> -Dhttps.proxyHost=<host> -Dhttps.proxyPort=<port> tomcat7:run

## Modificar la caché
Por defecto se almacenan en caché todas las peticiones `GET` para evitarlo, se debe anotar el método del controlador que no se quiera almacenar en caché con la anotación `@NoCache`, la configuración de la caché se encuentra en el fichero `src/main/resources/ehcache.xml`.

Por otro lado se puede utilizar la caché propia de spring:
* `@Cache(Cache.DURACION_1MIN)` el elemento se cachea durante 1 minuto
* `@Cache(Cache.DURACION_5MIN)` el elemento se cachea durante 5 minutos
* `@Cache(Cache.DURACION_30MIN)` el elemento se cachea durante 30 minutos
* `@Cache(Cache.DURACION_1DIA)` el elemento se cachea durante 1 día 
