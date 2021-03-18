# Proyecto Acolitame
## Introducción
Este es el backend del proyecto acolitame.La lógica relacionada al manejo de las empresas está implementada aquí y fue desarrollada en Spring Boot.La conexión a la base de datos será realizada con la ayuda de JPA. Se tendrá una clase controlador por clase en el modelo. La implementación de la lógica mencionada no incluye registro de usuarios, inicios de sesión y renderización de las diferentes vistas.
Para los métodos los parámetros de entrada estarán en la url o enviados en formato json de acuerdo a la circunstancias.

## Antes de Compilar
1. Cambiar todos los "localhost" por el nombre del dominio
2. En Aplication Properties cambiar el puerto a la escucha si se desea
3. En el mismo archivo establecer las propiedades de la base de datos Postgresql: puerto, usuario, clave
## Compilación
1. Instalar maven
2. Instalar la JVM si previamente no está instalada.
2. En la carpeta del proyecto ejecutar el comando "maven package".
## Ejecucion
### Como contenedor de docker

Dentro del repositorio se encuentra un `Dockerfile` el cual permite la creacion de una imagen de docker para ejecutar la aplicacion, los pasos son los siguientes:

1. Instalar docker si no se tiene ya instalado .
2. Dentro del `Dockerfile` cambiar el valor de *EXPOSE* para que sea el mismo que la variable *PORT* dentro del archivo `Aplication Properties`.
3. Ejecutar `docker build -t yourname .`, siendo `yourname` el nombre que se le asignara a la imagen de docker. Esto creara la imagen de docker que se utilizara para la ejecucion.
4. Ejecutar `docker run -dp 8080:PORT --name containerName yourname` para ejecutar un contenedor de docker con la imagen previamente creada. Siendo por el valor configurado de *EXPOSE* dentro de `Dockerfile`. el puerto previamente seleccionado es el 8080 del aplication properties. Como ya se mencionó puede ser cambiado.
## Autores

- Berenice Guerrero
- Marcelo Peñafiel
- Daniel Seaman (Ver historia de commits para ver contribuciones)

Estudiantes de Ingeniería en Sistemas de la Facultad de Ingeniería de la Universidad de Cuenca.

