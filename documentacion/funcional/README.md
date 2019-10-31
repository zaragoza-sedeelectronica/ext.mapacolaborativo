# 1. Descripción

Los Mapas Colaborativos son un instrumento de participación, colaboración y co-creación que facilita a los diferentes agentes (empresas, organizaciones ciudadanas, emprendedores, personas, etc.) acceder y utilizar los datos abiertos espaciales de Zaragoza para conocer su barrio y realizar propuestas para su mejora.
 
Uno de los objetivos es poner a disposición de la ciudadanía los elementos necesarios que permitan tener en cuenta sus aportaciones a la hora de tomar decisiones vinculadas a posiciones geográficas como por ejemplo: ubicación de un banco, suprimir una barrera arquitectónica, etc.
 
Este concepto se pretende extender al objeto de que sea la propia ciudadanía la que proponga que mapas quiere que se desarrollen, y participe en los mismos.
La ciudadanía puede de esta forma participar en el desarrollo y diseño de servicios, productos,...
 
La  deliberación,  la  búsqueda  de  ideas  y   los mapas colaborativos  nos  sirven  para  saber  qué  quiere   la   ciudadanía,   y  saber   con   quién   relacionarnos.   Debemos   abrir   las   políticas   públicas,   facilitando  que  el  papel  de  la  ciudadanía  sea  más  activo  y  ganar  en  calidad  de  vida  de  la   comunidad.  
 
La cartografía, concebida como herramienta colaborativa y de impulso en los procesos de gobernanza abierta  ha de considerar:

* Los **objetivos**. Hay que coordinar las **aspiraciones lógicas** y los **sentimientos de la ciudadanía** que vive y sufre ese espacio, y los **deseos y planteamientos** de los gestores **políticos**.
* La definición de las **variables**, que **pueden tener presentaciones no mensurables** y, sin embargo, de ellas puede depender el éxito o fracaso del análisis.
* La **escala** de presentación del problema. A diferente escala o ámbito de referencia, diferente grado de información y tratamiento de la misma. Esto es de vital interés si lo que se busca es la operatividad de los resultados, y la inteligibilidad de la cartografía resultante de los mismos
* El **carácter transversal y multifocal de los estudios urbanos**. Es necesario sopesar **todos los valores** dentro de un conjunto más amplio para que adquieran su auténtica dimensión y eficacia.
 
## 1.1 Los mapas colaborativos: la hibridación entre los cuantitativo y lo emocional

El desarrollo de las cartografías colaborativas puede tener componentes sensibles o emocionales ofreciendo instrumentos que combinan diferentes métodos de trabajo.

Algunos vendrán derivados de la información que podamos obtener a través de elementos como los sistemas de información geográfica, los visores cartográficos, las redes sociales o las herramientas biométricas. Igualmente, interesan las representaciones individuales o colectivas sobre mapas en blanco, o con información que podremos combinar con el juego o la participación individual y colectiva.

El visor, los sistemas de información geográfica y sus programas de cartografía digital deben de facilitar las representaciones cartográficas emocionales de una manera digital, en red y compartida por la ciudadanía.

Incluso se podrían monitorizar a través de herramientas biométricas las reacciones fisiológicas ante determinados estímulos o procesos de construcción de la propia cartografía emocional o colaborativa. Y todo ello lo lograremos representar a posteriori a través de los sistemas de información geográfica y la cartografía digital para que sea una capa más en el conocimiento de la ciudad ayudando a la gobernanza y a la toma de dicciones. Habría que reflexionar sobre tres tendencias:
 
* Ligada a la **psicogeografía** y los cambios fisiológicos o las respuestas fisiológicas de nuestro cuerpo y cerebro hacia determinados estímulos, sensaciones y emociones (Salud/calidad de vida).
* Articulada en torno al **significado e interpretación de los recuerdos** relacionados con la historia, o la historia vital de colectivos específicos (antropología/sociología).
* Cuestiones **artísticas y estéticas**

# 2. Componentes básicos

A continuación se detallan los componentes básicos para una herramienta que tiene que permitir la creación de mapas colaborativos e interactivos que sirvan a la ciudadanía para mapear sus valoraciones, emociones y percepciones de sus calles, barrios y ciudades.
 
Es importante que existan mapas interactivos que permitan combinar un gran volumen de datos y visualizaciones a la carta de la ciudadanía en múltiples pantallas y dispositivos.
 
## 2.1 Cartografía base

La ciudadanía puede acceder a la Infraestructura de datos espaciales de Zaragoza desde cualquier dispositivo. Una dificultad es comprender y aprovechar flujos de interacción y captación de datos de manera que sean útiles para la acción política y la gobernanza de las ciudades. Una de las oportunidades de una IDE (Directiva INSPIRE) es poder crear capas de información que mejoren la actuación y la intervención en lo público. Desde un punto de vista tecnológico, los servicios son la parte más importante de una IDE y proporcionan una base para la búsqueda, evaluación y explotación de la información espacial para usuarios y proveedores de todos los niveles siendo la base que permite construir aplicaciones para explotar los datos geográficos. Los servicios fundamentales, siempre conforme a la especificación de [OGC](http://www.opengeospatial.org/) son: 

* Servicios de visualización: 
	* Servicios Web de Mapas [WMS](http://www.opengeospatial.org/standards/wms). Estos servicios proporcionan una representación (imagen) de un mapa para un área requerida. Las capas se muestran y pueden solicitarse individualmente o mediante un mapa compuesto. 
	* Servicios Web de Teselas de Mapa [WMTS](http://www.opengeospatial.org/standards/wmts), de modo que son estándar e interoperables que proporcionan un acceso más rápido y eficiente y permiten ofrecer una mejor experiencia de uso.
* Servicios de Descarga
	* Servicios Web de Features WFS. Estos servicios facilitan datos que poseen un conjunto de características espaciales. Habitualmente los datos proporcionados están codificados mediante GML, pero cualquier otro tipo de dato y cualquier formato es válido. GML codifica vectores de datos (por ejemplo un servicio de features usual devuelve las coordenadas para datos geométricos, sin embargo, un servidor web de mapas devuelve una imagen). Además, los datos geométricos pueden ir acompañados de información no espacial.
 
En cuanto a los formatos de almacenamiento de datos geográficos se recomienda utilizar un sistema gestor de base de datos con módulo espacial como PostgreSQL (software libre) con POSTGIS o como Oracle Spatial para almacenar las geometrías como objetos espaciales nativos de dichos sistemas. Si no se puede utilizar ningún sistema de este tipo las geometrías se almacenarán en columnas de tipo BLOB o CLOB en formato WKB o WKT.

## 2.2 Capas de información

Son los conjunto de datos que se pueden incorporar a un mapa colaborativo. Se pretende facilitar la incorporación de recursos al mapa colaborativo favoreciendo que las personas puedan conocer y complementar la visión que tienen de la ciudad, y mejorar sus capacidades de análisis, reflexión y propuestas de mejora. Algunos ejemplos de conjuntos de datos en formatos abiertos que se pueden incorporar son:
 
* Distintas delimitaciones de la ciudad (juntas, distritos, manzanas y edificios)           
* Plano urbanístico (PGOU, catastro)       
* Equipamientos y servicios municipales                        
* Infraestructuras del espacio público (agua, electricidad, zonas verdes, farolas, aceras, arbolado, bancos, contenedores, etc.)                     
* Redes de transporte                      
* Otros (equipamientos privados, etc.)     

## 2.3 Tipologías o niveles de participación y co-creación
 
Las posibles tipologías o niveles de participación y co-creación que la ciudadanía tiene a su disposición, a través de los mapas colaborativos, para el diseño de sus calles, barrios y ciudad son:
 
* **Sugerencias**. Serían peticiones objetivas y justificadas. Sobre las bases de información existente: la ciudadanía selecciona una de los items ya existentes y sobre el mismo hace una sugerencia (estado en el que se encuentra una infraestructura, equipamiento o servicio. El estado de un equipamiento como un banco estropeado, baldosas o firme en mal estado, una mejora del servicio,…).
* **Propuestas**. Sobre un tema como la propuesta para localizar unos bancos, o una farola porque se considera que la zona está mal iluminada se hace una votación, reflexión, análisis en el que participa la ciudadanía. E incluso puede hacer sugerencias utilizando la herramienta anterior con el modelo de propuesta. 
* **Percepción**. Es la valoración subjetiva del espacio de acuerdo al modelo de emociones o sentimientos (Alegría, tristeza/nostalgia, miedo, rabia, rechazo/asco, sorpresa). Ideal para trabajar sobre un espacio, una infraestructura sobre la que se quiere expresar su emoción. Puede ser sobre un tema de valoración general (espacios que te dan miedo de la ciudad, olores, ruidos) o acciones más específicas para considerar más allá del proyecto urbanístico, las consideraciones más cualitativas del ciudadano.  La representación cartográfica es más general, pero ofrecer información muy interesante independientemente del estado o calidad de la infraestructura, equipamiento o espacio. 
* **Cuadros de mandos o fuentes de experiencias participativas**.  Existen numerosas iniciativas de colectivos -muchos de ellos con financiación pública- que están recogiendo información más o menos georreferenciadas o la percepción del barrio, y que serviría de recopilación para tenerlo en un sitio web (Las Zaragozas, El semáforo de San Pablo, los de los Centros de Salud, mapas activos de Salud, Encuestas....).

## 2.4 Usuarios/usuarias del servicio

Existen dos tipos de usuarios/as del servicio del módulo de mapas colaborativos. Los tipos de usuarios que se pueden definir son:

* Usuarios/as anónimos/as: cualquier usuario que acceda a la URL del sistema sin autenticar en la plataforma de Gobierno Abierto puede acceder a los mapas, consultando el contenido de los mismos pero no podrá ni crear ni modificar mapas.
* Usuarios/as autenticados: los usuarios/as dados de alta en la plataforma de Gobierno Abierto del Ayuntamiento pueden, además de consultar los mapas disponibles, colaborar en la co-creación de mapas existente, de crear y editar nuevos mapas. La edición sólo se puede realizar en:
	* mapas definidos por un mismo usuario/a
	* mapas definidos por otros usuarios/as y definidos como colaborativos y por lo tanto facilitan la co-creación.

## 2.5 Tipos de mapas

El sistema de mapas colaborativos permite definir tres tipos de mapas. Los tipos de mapas colaborativos disponibles son:

* **Públicos**: pueden ser consultados por todos los usuarios/as pero no permiten la co-creación, colaboración de los mismos.
* **Colaborativos**: pueden ser consultados por cualquier usuario/a y además permiten la modificación y la co-creación de elementos por parte de usuarios/as de la plataforma de Gobierno Abierto.
* **Privados**: mapas sólo disponibles para el usuario que los ha definido, no permite el autor que otros participen en su creación, no permiten la colaboración en su edición.

# 3. Funcionalidades del sistema

El sistema de mapas colaborativos ofrece una serie de funcionalidades:

* Listado de mapas colaborativos
* Creación y co-creación de mapas colaborativos
* Consulta de un mapa colaborativo
* Modificación/Eliminación de mapas colaborativos  

A continuación se irán comentando cada una de estas funcionalidades y las posibles mejoras funcionales para cada una de las funcionalidades. 

## 3.1  Listado de mapas colaborativos
Esta funcionalidad permite consultar el listado de mapas colaborativos disponibles en el sistema. Esta funcionalidad permite:

* consultar, en la página principal del sistema, el listado de mapas públicos (colaborativos o no) desarrollados por ciudadanos
* cuando un usuario se autentica en el sistema, consultar los mapas que ha desarrollado

Sobre el listado de mapas que aparece en la pantalla principal, se pueden realizar diferentes filtros:

* ver todos los mapas, opción por defecto al entrar en la página
* seleccionar sólo los mapas colaborativos, usando la opción “Mapas en los que puedo colaborar co-creando”
* buscar mapas por el título del mismo, usando el cuadro de búsqueda
* filtrar mapas por categorías, si debajo del cuadro de búsqueda hay alguna categoría mostrada

Este listado de mapas de la pantalla principal, muestra un listado de mapas, tres por cada línea, donde cada mapa está representado como una ficha que muestra:

* el nombre del mapa, pulsando sobre el título del mapa, se accede a la página de detalle del mapa
* la fecha de la última actualización del mapa
* un icono para compartir la información básica del mapa, título y URL del mapa, en diferentes redes sociales solicitando la colaboración, co-creación de otros o informando de un mapa cerrado.

Además del listado de la página principal, existe otro tipo de listado de mapas. Este listado es accesible para usuarios autenticados en la plataforma y se accede pulsando en la página principal en la opción “Mis Mapas”. Mediante esta opción, se muestra el listado de mapas asociadas al usuario con una estructura similar al listado de la página principal pero sin la opción de compartir la ficha del mapa colaborativo en las redes sociales.

### 3.1.1       Posibles mejoras funcionales

Una vez indicadas las funcionalidades disponibles en el listado de mapas, se pasa a enumerar nuevas funcionalidades que podrían añadirse al sistema actual:

* nuevas opciones de filtrado: actualmente se puede filtrar por mapas colaborativos que permiten la co-creación, por categoría, si existe alguna categoría disponible del cuadro de búsqueda, pero no existe un desplegable que permite elegir entre todas la categorías disponibles para los mapas colaborativos. Estas categorías, listadas en las pantallas de generación o modificación de mapas, son: cultura, deporte, educación, hostelería, ocio y turismo.
* no existe opción de ordenación del listado de mapas, bien por nombre, por fecha de actualización, etc.
* posibilidad de seleccionar el número de mapas a mostrar en el listado
* una curiosidad que se ha detectado es que, si bien en la pantalla de listado general cada mapa tiene la opción de compartir en redes sociales, cuando se selecciona el listado de mapas de un usuario en concreto, no se muestra dicha opción.
 
## 3.2 Creación de mapas colaborativos

Esta funcionalidad permite a los usuarios/as de la plataforma de Gobierno Abierto generar mapas colaborativos. Cuando se genera un mapa colaborativo, se debe introducir los siguientes elementos:

* nombre del mapa
* tipo de mapa: define la visibilidad del mapa:
	* publicado: visible para todos los usuarios pero sólo puede ser modificado por el usuario que lo generó
	* oculto: visible sólo para el usuario que lo generó
	* colaborativo: visible para todos los usuarios y facilita la co-creación de los elementos por todo los usuarios de la plataforma de Gobierno Abierto
* Categoría: permite elegir la categoría asociada al mapa entre seis opciones, cultura, deporte, educación, hostelería, ocio y turismo
* Icono: permite elegir el icono asociado al mapa que se mostrará en la ficha del listado de mapas de la página principal
* Elementos geométricos en el mapa: existen diferentes elementos geométricos que pueden añadirse al mapa. Existen cuatro tipos de elementos, estos son:
	* Punto geométrico: un único punto geométrico al que se le puede asociar un nombre, una descripción y el tipo de icono asociado al punto
	* Poli línea: se define la línea de puntos geométricos y se le puede asociar una nombre, una descripción y se puede decidir el color de la línea y su opacidad
	* Polígono: se definen los puntos que forman el polígono y se le puede asociar un nombre, una descripción así como el color y la opacidad del borde y del relleno del mismo
	* Rectángulo: se le puede asociar un nombre, una descripción así como el color y la opacidad del borde y del relleno del mismo

Ésta es la información necesaria para generar un mapa colaborativo, que permite la co-creación entre diferentes personas en el sistema. No se puede añadir más información. Como se puede ver, es un proceso muy sencillo pero efectivo.

### 3.2.1 Posibles mejoras funcionales

Una vez indicadas las funcionalidades disponibles en la creación de mapas, se pasa a enumerar nuevas funcionalidades que podrían añadirse al sistema actual:

* Posibilidad de añadir una descripción asociada al mapa que permita una mayor comprensión del contenido del mismo. Puede que el título del mapa no sea suficientemente descriptivo o que se necesite más información para comprender el contenido del mismo
* Posibilidad de asociar URLs al mapa para completar la información
* Posibilidad de asociar a la ficha descriptiva del  punto una imagen.
* Posibilidad que los usuarios puedan añadir comentarios a la geometría
* Posibilidad de crear un mapa colaborativo y pintar sobre el información asociada a un conjunto de datos en formatos abiertos.
 
## 3.3 Consulta de un mapa colaborativo

Esta funcionalidad permite consultar los mapas colaborativos disponibles en el sistema. Cuando se selecciona un mapa, la información que se muestra en pantalla es la siguiente:

* Título del mapa
* El mapa geográfico con todos los elementos definidos en él. En el mapa se pintan todos los elementos geográficos, permitiendo pinchar en cada uno de ellos para consultar el nombre y la descripción asociada a cada elemento geográfico
* Una barra para compartir el mapa geográfico.  Las opciones que se ofrecen son:
	* Un código HTML para embeber el mapa en otra página HTML
	* En formato XML con toda la información del mapa
	* En formato JSON con toda la información del mapa

Además de mostrar la información del mapa, en la página de detalle de un mapa se puede mostrar el botón de “Editar” mapa. Este botón está disponible si el mapa a consultar es de tipo colaborativo o si un usuario está consultando alguno de los mapas que ha creado en el sistema. Si se selecciona esta opción, se pasará al modo de edición de mapas.

### 3.3.1 Posibles mejoras funcionales

Una vez indicadas las funcionalidades disponibles en el detalle de mapas, se pasa a enumerar nuevas funcionalidades que podrían añadirse al sistema actual:

* Mostrar la categoría asociada al mapa
* Mostrar el usuario/a que ha generado el mapa
* Mostrar la fecha de actualización del mapa
* Añadir las opciones de compartir en redes sociales que hay en las fichas de los mapas en el listado de mapas de la pantalla principal
* Posibilidad de cargar una capa de información asociada a un conjunto de datos en formatos abiertos, reutilización de nuestros conjuntos de datos. Tal vez un botón asociado a nuestro catálogo de datos abiertos…. 

## 3.4 Modificación/Eliminación de mapas colaborativos

Esta funcionalidad permite modificar o eliminar mapas colaborativos creados por los usuarios/as del sistema. La opción de edición sólo está disponible para los mapas de tipo colaborativo o mapas propios de un usuario y pueden realizarla sólo los usuarios autenticados en la plataforma de Gobierno Abierto. Las opciones de edición de un mapa dependen del usuario/a y del tipo de mapa que se esté consultando. Según estas opciones, las opciones de modificación son:

* Si un usuario/a quiere editar un mapa propio, puede modificar todos los elementos del mapa, a saber:
	* Nombre
	* Tipo
	* Categoría
	* Icono
	* Elementos geográficos del mapa
* Además, tienen disponibles dos opciones más de edición:
	* Duplicar: para crear una réplica del mapa actual
	* Eliminar: para borrar el mapa actual
* Si un usuario/a intenta modificar un mapa de tipo colaborativo del que no es el creador, sólo puede modificar los elementos geográficos del mapa

### 3.4.1 Posibles mejoras funcionales

Para este apartado no se han identificado nuevas funcionalidades que añadir. Sólo deberían añadirse si, por ejemplo, se aceptasen las sugerencias hechas en el apartado de creación de mapas, como por ejemplo una descripción asociada al mapa.

* Revisar la idea de poder cargar capas de información asociadas al catálogo de datos abiertos...como hemos comentado en apartados anteriores

# 4. Funcionalidades a nivel general

Además de las nuevas funcionalidades sugeridas anteriormente, existen otras funcionalidades que podrían ser interesantes para el sistema de mapas colaborativos. Estas nuevas funcionalidades son:

* Elección de un grupo de usuarios para que puedan participar, colaborar en la co-creación de los mismos.
* Realización de comentarios sobre cada punto de interés .
* Realización de comentarios generales sobre los mapas.
* Implementar un sistema de valoración de utilidad/interés de los mapas para que lo usuarios voten. Podría ser un sistema sencillo en plan “Si/no” “Pulgar arriba/pulgar abajo”
* Facilitar la edición de capas de información asociadas al catálogo de datos abiertos….
