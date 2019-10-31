Antes de empezar con el manual de instalación del sistema de mapas colaborativos, conviene indicar que hay:

* un manual de uso disponible en el siguiente enlace [src/main/java/org/sede/servicio/mapacolaborativo/manual/README.md](manual/README.md)
* un análisis funcional del sistema disponible en el siguiente enlace [src/main/java/org/sede/servicio/mapacolaborativo/funcional/README.md](funcional/README.md)

# Manual de instalación

Para la instalación del sistema de mapas colaborativos, se utilizará el manual general de instalación disponible en la raíz del proyecto [/src](/zaragoza/sede/src/master/). En el manual general se indican los pasos a seguir para la instalación del general del proyecto. En este manual, se indicarán los pasos necesarios para que el sistema de mapas colaborativos esté listo para funcionar.

# Pasos a seguir

Primero, hay que realizar los siguientes pasos del manual general:

* comprobar versiones de Java y maven
* instalar eclipse y maven intagration para eclipse
* clonar repositorios:
    * sede
    * assets
    * fragmentos
    * servicio
    * portal
    * i18n (internalización)
* instalación de librerías en repositorio local
* configuración de ficheros de propiedades

Según el [manual general](/zaragoza/sede/src/master/), el siguiente paso sería lanzar el servidor de desarrollo. Antes de este paso, hay que realizar una serie de pasos necesarios.

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

* noticias: los ficheros a usar están en [src/main/java/org/sede/servicio/acceso/](/zaragoza/sede/src/master/src/main/java/org/sede/servicio/acceso/)
* participacion: los ficheros están disponibles en [src/main/java/org/sede/servicio/mapacolaborativo/](.)

El orden de ejecución es el siguiente:

* noticias:
    * db.sql
    * users_db.sql
* participacion:
	* db.sql
	* package.sql
	* package_body.sql
	* data.sql
	
Una vez realizados estos pasos, ya estaría lista la base de datos en Oracle. Ahora toca configurar los ficheros de bases de datos en el proyecto sede. Este paso está descrito en el apartado **Conexión a BBDD** del [manual general](/zaragoza/sede/src/master/).

# Probar el módulo

El módulo se puede probar de dos formas diferentes, lanzando el servidor y ver la página principal del modulo o usando las pruebas unitarias:

* para lanzar el servidor, como se indica en el [manual general](https://bitbucket.org/zaragoza/sede/src/master/), hay que usar el comando *$ mvn -Dmaven.tomcat.port=8888 tomcat7:run* en el directorio del proyecto y abrir la página web http://localhost:8888/sede/servicio/mapa-colaborativo y debería cargarse la página principal
* para lanzar las pruebas unitarias, hay que ejecutar el fichero [src/test/java/org/sede/servicio/mapacolaborativo/MapaColaborativoTest.java](/zaragoza/sede/src/master/src/test/java/org/sede/servicio/mapacolaborativo/MapaColaborativoTest.java)

Con estos pasos, ya se podría utilizar el módulo de mapas colaborativos. Para el resto de opciones de configuración, Apache, trabajar con ficheros estáticos, etc, consultar el [manual general](https://bitbucket.org/zaragoza/sede/src/master/).
