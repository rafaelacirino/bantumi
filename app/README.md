# Bantumi - Aplicación Android

## Autora: Rafaela Borba Falcão Cirino

## Índice
1. [Sobre el Proyecto](#sobre-el-proyecto)
2. [Funcionalidades](#funcionalidades)

## Sobre el Proyecto

**Bantumi** es una implementación del tradicional juego de mesa *Mancala* desarrollado para la plataforma Android. La aplicación permite a los jugadores competir entre ellos o contra la máquina, guardar y recuperar partidas, además de visualizar las mejores puntuaciones.

## Implementaciones

- **Reiniciar Partida**: Fue implementada en la clase Main la opción de reiniciar partida dentro del método onOptionsItemSelected.
- **Guardar y Recuperar Partida**: Fue creada la clase GuardarPartida que será la responsable para esta funcion. En la clase JuegoBantumi se fueron implementados los métos serializa y deserializa. En la clase Main en el método onOptionsItemSelected fueron añadidas las opciones de guardar y recuperar partido. Y también fueron añadidas nuevas strings en el archivo strings.xml. 
- **Guardar Puntuación**: Fue añadido en el archivo build.gradle dependencias de android.room. En el paquete de datos fueron creadas las clases Bantuni, BantumiDAO, BantumiRepositorio, BantumiDatabase e la clase Converters que es necesaria para converter la fecha y guardar devidamente en el banco de datos. En la clase Main fue añadido un método para guardar la puntuación y fue modificado el método finJuego. Fueron añadidas strings en el archivo strings.xml.
- **Mejores Resultados**: Fue implementada la nueva activity MejoresResultadosActivity que tiene la responsabilidad de implementación de la lógica de los mejores resultados. En la clase Main fueron añadidas dos opciones para solicitar la opción de mejores resultados y otra opción para borrar la pantalla de mejores resultados. Fueron añadidos dos métodos en la clase BantumiViewModel para obtener los 10 mejores resultados y otro de borrar los mejores resultados. Fue creado un layout de mejores resultados (activity_mejores_resultados). Fueron añadidas nuevas strings en el archivo strings.xml y en el archivo AndroidManifest fue añadida esta nueva activity creada, MejoresResultadosActivity.
- **Elejir Jugador**: Fue implementada una nueva activity, ElejirJugadorActivity; con la lógica de elijir el jugador que empezará el partido y añadida en el manifesto para que ella sea la activity que iniciará la aplicación, o sea, el launch. Fue añadido en la clase Main en el método onCreate la elección de jugador. Fue implementada la activity para ElejirJugador y añadidas nuevas strings en el archivo strings.xml.
- **Cambiar Color**: Implementación de la opción de cambiar color. Fue añadido en el método onOptionsItemSelected la opción de cambiar color y fueron añadidos los métodos mostrarDialogoCambiarColor y cambiarColor. En el archivo de menu, opciones_menu, fue añadido la opción de cambiar color y añadidas nuevas strings en el archivo strings.xml.