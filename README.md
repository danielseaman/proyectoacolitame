# Proyecto Acolitame
## Introducción
Este es el backend del proyecto acolitam,La lógica relacionada al manejo de las empresas está implementada aquí y fue desarrollada en Spring Boot.La conexión a la base de datos será realizada con la ayuda de JPA. Se tendrá una clase controlador por clase en el modelo. La implementación de la lógica mencionada no incluye registro de usuarios, inicios de sesión y renderización de las diferentes vistas.
Para los métodos los parámetros de entrada estarán en la url o enviados en formato json de acuerdo a la circunstancias.

## Antes de Compilar
1. Cambiar todos los "localhost" por el nombre del dominio
2. En Aplication Properties cambiar el puerto a la escucha si se desea
3. En el mismo archivo establecer las propiedades de la base de datos Postgresql: puerto, usuario, clave
## Compilación
1. Instalar maven
2. Instalar la JVM si previamente no está instalada.
2. En la carpeta del proyecto ejecutar el comando "maven package".
3. Ejecutar.
