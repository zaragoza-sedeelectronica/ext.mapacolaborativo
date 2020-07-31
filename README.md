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

## Instalar y comprobar versiones software

Las versiones mínimas del software son:

* Java 1.8
* Maven >= 3.0.5
* Oracle 11
* Eclipse 2019-03 (recomendada)

Una vez instalado Eclipse, hay que instalar  maven integration for eclipse desde la siguiente url http://download.eclipse.org/releases/indigo/ y seleccionando la opción "Generarl Purpose -> m2e"

## Configurar la base de datos

El sistema de mapas colaborativos utiliza un usuario general para las bases de datos. Este ususario permite la gestión de los datos de usuarios de la plataforma y los datos de los mapas colaborativos.

Por lo tanto, hay que configurar una única base de datos para que el modulo funcione correctamente. Para eso hay que realizar los siguientes pasos:
* generar el usuario en Oracle y otorgarle privilegios
* ejecutar los scripts de generación de las bases de datos
* configurar las bases de datos en el proyecto opencity.ext.web

A continuación se detallan los pasos a seguir en cada uno de ellos.

## Generar los usuarios en Oracle

Como se indica en el apartado anterior, hay que generar un usuario para la base de datos. El usuario se debe generar en Oracle ya que el sistema de mapas colaborativos utiliza Oracle.

Se aconseja que el nombre del usuario sea "general" ya que es el que se utiliza en el core de la aplicación. A continuación se muestran los comandos que deben utilizarse en la consola de SQL*Plus, si se utiliza dicha consola para realizar este paso, o si se realiza desde SQLDeveloper o similar deberemos utiizar el usuario de Sistema (normalmente SYS) con privilegio para crear esquemas.

Para generar el usuario general se utilizarían los comandos de abajo, 

```
CREATE USER general IDENTIFIED BY "password";
GRANT ALL PRIVILEGES TO general;
```
o bien, si se quieren especificar los tablespaces a asignar al usuario y el tamaño que ocuparán estos en base ed datos se puede ejecutar el script **0.schema_general.sql** disponible en la carpeta [scripts-bbdd/](scripts-bbdd/). 


## Ejecutar los scripts de generación de las bases de datos

El siguiente paso es crear las tablas y cargar los datos de prueba disponibles para este módulo. Los scripts que se van a importar están disponibles en la carpeta [scripts-bbdd/](scripts-bbdd/).

El orden de ejecución es el siguiente:

* 1.db.sql
* 2.users_db.sql
* 3.users_data.sql
* 4.extra_users.sql
* 5.db.sql
* 6.data.sql
* 7.package.sql
* 8.package_body.sql
* extra_users.sql
* test_data.sql
	
Si ejecutamos los scripts por primera vez no deberíamos obtener errores en la ejecución, lo que nos garantiza que la estuctura y datos son corectas. 

## Clonar repositorios:

A continuación se listan los repositorios a clonar: 

* mapas colaborativos: https://<tuUsuario>@bitbucket.org/zaragoza/mapas-colaborativos.git
(Puedes encontrar la url real en la cabecera de la página al acceder al proyecto desde bitbucket.org)

## Uso de Internacionalización i18n

El proyecto utiliza la internacionalización de mensajes, la configuración de esta funcionalidad está definida en el proyecto ext.core.web para el cual debe existir un fichero para cada idioma, por ejemplo, `messages_es.properties` para el castellano. 

En mapas colaborativos los ficheros se encuentran en la  ruta **/ext.web.mapacolaborativo/src/main/webapp/i18n/**
```
	prueba=texto en castellano
	prueba2=otro texto en castellano
```

*Nota:*
El contenido de estos ficheros se actualiza según el parámetro `cacheSeconds` en al configuración del modulo ext.core.web, concretamente en el archivo 
**/ext.core.web/src/main/java/org/sede/config/WebConfig.java** , si se desea cambiar la configuración será necesario importar el proyecto est.core.web 
a nuestro IDE de desarrollo, y una vez modificada, realizar un *mvn install* sobre el mismo para actualizar las librerías del repositorio local de Maven **.m2** y hacer un (boton derecho sobre el proyecto ext.web.mapacolaborativo en Eclipse) para pulsar sobre *Manven/Update Project* y actualizar así el nuevo .jar generado en nuestro proyecto de mapas.


## Instalación de librerías en repositorio local:

Existen librerías que no están disponibles en repositorios maven, son las que se encuentran en la carpeta [librerias](librerias/) y se deben instalar en el repositorio maven,  para ello, nos situamos en el directorio **librerias/** y usando un terminal ejecutamos las siguientes líneas:

```
mvn install:install-file -DgroupId=org.zaragoza -DartifactId=opencity.ext.core.web -Dversion=0.0.1 -Dpackaging=jar -Dfile=opencity.ext.core-web-0.0.1.jar -DgeneratePom=true
mvn install:install-file -DgroupId=org.zaragoza -DartifactId=opencity.ext.core -Dversion=0.0.1 -Dpackaging=jar -Dfile=opencity.ext.core-0.0.1.jar -DgeneratePom=true
mvn install:install-file -DgroupId=org.zaragoza -DartifactId=opencity.ext.core.test -Dversion=0.0.1 -Dpackaging=jar -Dfile=opencity.ext.core.test-0.0.1.jar -DgeneratePom=true
mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc5 -Dversion=11.2.0 -Dpackaging=jar -Dfile=ojdbc5.jar -DgeneratePom=true
mvn install:install-file -DgroupId=virtuoso.jena.driver -DartifactId=virtjdbc -Dversion=3 -Dpackaging=jar -Dfile=virtjdbc.jar -DgeneratePom=true
mvn install:install-file -DgroupId=virtuoso.jena -DartifactId=virt_jena -Dversion=3 -Dpackaging=jar -Dfile=virt_jena.jar -DgeneratePom=true
mvn install:install-file -DgroupId=idezar -DartifactId=geoapi -Dversion=2.0 -Dpackaging=jar -Dfile=geoapi.jar -DgeneratePom=true
mvn install:install-file -DgroupId=idezar -DartifactId=geoapi-nogenerics -Dversion=2.1-M2 -Dpackaging=jar -Dfile=geoapi-nogenerics.jar -DgeneratePom=true
mvn install:install-file -DgroupId=idezar -DartifactId=deegree2-pre -Dversion=2 -Dpackaging=jar -Dfile=deegree2-pre.jar -DgeneratePom=true
mvn install:install-file -DgroupId=idezar -DartifactId=gsl-coordinate-transformation -Dversion=1.0-jdk15 -Dpackaging=jar -Dfile=gsl-coordinate-transformation.jar -DgeneratePom=true
mvn install:install-file -DgroupId=idezar -DartifactId=gt2-epsg-wkt -Dversion=2.3.5 -Dpackaging=jar -Dfile=gt2-epsg-wkt.jar -DgeneratePom=true
mvn install:install-file -DgroupId=idezar -DartifactId=gt2-referencing -Dversion=2.3.0 -Dpackaging=jar -Dfile=gt2-referencing.jar -DgeneratePom=true
mvn install:install-file -DgroupId=idezar -DartifactId=iaaa_csct -Dversion=1.6.2 -Dpackaging=jar -Dfile=iaaa_csct.jar -DgeneratePom=true
mvn install:install-file -DgroupId=idezar -DartifactId=j3dcore -Dversion=1.3.0 -Dpackaging=jar -Dfile=j3dcore.jar -DgeneratePom=true
mvn install:install-file -DgroupId=idezar -DartifactId=j3dutils -Dversion=1.3.0 -Dpackaging=jar -Dfile=j3dutils.jar -DgeneratePom=true
mvn install:install-file -DgroupId=idezar -DartifactId=jai_codec -Dversion=1.1.1_01 -Dpackaging=jar -Dfile=jai_codec.jar -DgeneratePom=true
mvn install:install-file -DgroupId=idezar -DartifactId=jai_core -Dversion=1.1.1_01 -Dpackaging=jar -Dfile=jai_core.jar -DgeneratePom=true
mvn install:install-file -DgroupId=idezar -DartifactId=jGridShiftApi -Dversion=2.0 -Dpackaging=jar -Dfile=jGridShiftApi.jar -DgeneratePom=true
mvn install:install-file -DgroupId=idezar -DartifactId=jsr108 -Dversion=0.01 -Dpackaging=jar -Dfile=jsr108.jar -DgeneratePom=true
mvn install:install-file -DgroupId=idezar -DartifactId=jts -Dversion=1.6 -Dpackaging=jar -Dfile=jts.jar -DgeneratePom=true
mvn install:install-file -DgroupId=idezar -DartifactId=vecmath -Dversion=1.3.1 -Dpackaging=jar -Dfile=vecmath.jar -DgeneratePom=true

```

## Configuración del contenido estático en Apache
Una vez clonado el proyecto, se habran generado una sertie de carpetas en nuestro repositorio local, en la ruta **\opencity.ext.web\src\main\webapp** encontramos
el contenido estático de la aplicación, el cual, para mostrarse correctamente una vez desplegada debe servirse mediante un servidor Apache haciendo uso 
de su funcionalidad ProxyPass. Para ello, una vez instalado Apache, creamos una carpeta llamada **cont** dentro del directorio de publicacion *htdocs* y dentro 
de esta colocamos el contenido estático seguiendo la siguiente estructura:

```
cont
|--- assets (se coje el contenido de la carpeta opencity.ext.web\src\main\webapp\assets)
cont/vistas
|--- fragmentos (se coje el contenido de la carpeta opencity.ext.web\src\main\webapp\fragmentos)
|--- servicio (se coje el contenido de la carpeta opencity.ext.web\src\main\webapp\servicio)
```

Una vez realizado este paso, hay que configurar un servidor apache para que pueda servir contenidos estáticos habilitando un proxy inverso e indicando su configuración.
En archivo de configuración de Apache **conf/httpd.conf** (en Windows), primero habilitamos los modulos descomentando las siguientes líneas:

```
LoadModule proxy_module modules/mod_proxy.so
LoadModule proxy_http_module modules/mod_proxy_http.so
```
Al final del mismo fichero de configuración añadimos las siguientes lineas:

```
ProxyPass /opencityext http://localhost:7777/opencityext
ProxyPassReverse /opencityext http://localhost:7777/opencityext
AddDefaultCharset utf-8
```

Además, añadimos la definicion del directorio y el Alias en el mismo archivo de configuración de apache:
```
Alias /cont ${SRVROOT}/htdocs/cont	
<Directory  ${SRVROOT}/htdocs/cont>
	Options Indexes FollowSymLinks
	AllowOverride None
	Require all granted
</Directory>
```

## Configuración del módulo

Lo primero que debemos hacer es ejecutar:

```
mvn clean install
```

dentro del proyecto `opencity.ext.mapacolaborativo` 

Una vez instalado hay que modificar/crear los siguientes ficheros de configuración en el modulo web:

`/ext.web.mapacolaborativo/src/main/resources/META-INF/context.xml`
`/ext.web.mapacolaborativo/src/main/resources/application.properties`

Para obtener un ejemplo/plantilla de estos archivos de configuración podemos consultar o descargar el proyecto  **../zaragoza/shared-resources.git**

El contenido del fichero **context.xml** contiene los datos de conexión a la base de datos y debe ser el siguiente:

```
<?xml version="1.0" encoding="UTF-8"?>
<Context>
<Resource name="jdbc/WebGeneralDS" auth="Container"
              type="javax.sql.DataSource" driverClassName="oracle.jdbc.OracleDriver"
              url="jdbc:oracle:thin:@localhost:<puerto>/<SID>"
              username="general" password="<pass>" maxActive="20" maxIdle="10"
              maxWait="-1"/>
</Context>
```
sustituyendo **puerto**, **SID** y **pass** por los datos de nuestra configuración. 
Ejemplo a la hora de realizar este documento: 

```
              url="jdbc:oracle:thin:@localhost:1521/orcl"
              username="general" password="general" maxActive="20" maxIdle="10"
```

El contenido del fichero application.properties debe ser el siguiente:

```
contexto=/opencityext
path=localhost
thymeleaf.view=<ruta-opencity.ext.web>/src/main/webapp/vistas/
thymeleaf.strictMode=false
path.i18n=<ruta-opencity.ext.web>/src/main/webapp/i18n
datasource.prefix=java:/comp/env/
path.solr=www.zaragoza.es
path.allowed=localhost:7777,localhost,localhost:9999,localhost:7001
solr.usuario=
solr.password=
proxy.host=
proxy.port=
mail.server=
mail.user=
mail.pass=
entorno=local
path.cont=http://localhost:<apache-port>/cont
path.cont.external=http://localhost:<apache-port>/cont/
path.cont.disk=<ruta-apache-httdocs>/cont
path.vistas.disk=<ruta-apache-httdocs>/cont/vistas
path.aplicaciones.disk=<ruta-apache-httdocs>/cont/aplicaciones/
virtuoso.sparql=http://datos.zaragoza.es/sparql
virtuoso.user=
virtuoso.pass=
virtuoso.connection=
sms.server=
```
Sustituyendo el contenido de  `ruta-opencity.ext.web` y  `ruta-apache-httdocs` por las rutas relativas/absolutas a cada carpeta.

Esta definición se puede realizar para cada uno de los entornos en `resources`, `resources-dev`, `resources-prod` y `resources-test`.


# Prueba del módulo

## Pruebas unitarias

Para una prueba rápida del módulo, se pueden ejecutar las pruebas unitarias que hay en el proyecto opencity.ext.mapacolaborativo. Para poder ejecutar la prueba, hay que configurar los siguientes campos en el fichero `opencity.ext.mapacolaborativo/src/test/resources/test.properties`:

* conexion.jdbc para indicar la conexión con la base de datos
* db.general.pass para indicar la contraseña del usuario general creado anteriormente

El resto de campos del fichero no deberían modificarse.

Una vez modificado, se pueden lanzar las pruebas unitarias seleccionando la clase `opencity.ext.mapacolaborativo/src/test/org/sede/servicio/MapaColaborativoApiTest`, pulsar el botón derecho y seleccionar `Run as --> JUnitTest`

## Pruebas módulo web

Para lanzar el módulo, hay que ejecutar la siguiente instrucción desde la carpeta **/opencity.ext.web**:
```
$ mvn -Dmaven.tomcat.port=7777 tomcat7:run
```

Para probar que funciona correctamente acceder a:
```
http://localhost:7777/opencityext/servicio/mapa-colaborativo/
```

Puede ser que haya elementos que no se muestren correctamente por lo que se aconseja que mejor se utilice: 
```
http://localhost:<apache-port>/opencityext/servicio/mapa-colaborativo/
```
sustituyendo `apache-port` por el puerto habilitado en Apache mediante la intrucción **Listen XX** utilizada en el archivo de configuración httpd.conf (Windows).

Ejemplos:
En Apache Listen 80    -->  http://localhost/opencityext/servicio/mapa-colaborativo/
En Apachae Listen 8090 -->  http://localhost:8090/opencityext/servicio/mapa-colaborativo/

siempre asegurándonos de que el servidor Apache está arrancado.  `httpd -k start`

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
