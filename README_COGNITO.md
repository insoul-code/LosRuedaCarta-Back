### Cognito

En este bloque se añade lo necesario para poder verificar que el token que viene en la cabecera de la petición es válido. Esta verificación se hace consultando el grupo de usuarios de amazon cognito. Si se determina que el token es valido se permite el acceso a la API.

#### Requerimientos: 

 - Proyecto Gradle (Como mínimo la estructura del Proyecto) - Bloque raíz CQRS
 
#### Estructura del bloque: 

Dentro de la estructura del bloque se identifican las siguientes clases:

### Comun
- **ExcepcionAutenticacion**: Clase que lanza una excepción en caso de que el token no se encuentre en el header de la petición http, las credenciales con las que esta intentando acceder no existan o que el JWT Token no sea un Id token.
- **ConfiguracionFiltro**: Realiza validación de el endpoint recibido.
- **ConfiguracionJWT**: Ejecuta la configuración correspondiente al tiempo conexión, tiempo de lectura y URL para acceder al grupo de usuarios de AWS con el fin de validar el token que se recibe en las cabeceras de las peticiones.
- **FiltroValidacionToken**: Filter de Java que se encarga de obtener de la solicitud HTTP el token y verificar su validez.
- **ValidadorToken**: Verifica que el token sea de tipo ID.


