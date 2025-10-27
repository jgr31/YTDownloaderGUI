# YTDownloaderGUI

Author: Jordi Gelabert Roselló

Prototype Swing GUI wrapper for yt-dlp. Built with NetBeans 27 / JDK 24+ using Designer (null layouts).
See `Help > About` for citations.

## Build & Run

```bash
mvn -q -DskipTests package
java -jar target/YTDownloaderGUI-1.0-SNAPSHOT.jar
```

## Notes
- Uses `java.util.prefs.Preferences` for settings.
- Downloads run via `ProcessBuilder` in a `SwingWorker`.
- Last file can be opened via default OS media player.

## Compliance checklist (Enunciado)

- [x] JFrame principal, tamaño fijo, **no resizable**, **null layout**.
- [x] Componentes: JLabel, JButton, JTextField, JTextArea, JCheckBox, JRadioButton, ButtonGroup, JOptionPane, JFileChooser, JMenuBar, JMenu, JMenuItem, JFrame, JPanel, JDialog.
- [x] Menú: **File > Exit**, **Edit > Preferences**, **Help > About**.
- [x] **Preferences** como **JPanel** que **sustituye** al contenido del **JFrame** (no JDialog).
- [x] **About** como **JDialog modal** con autor, curso y citas (yt-dlp, ffmpeg, etc.).
- [x] **Reproductor por defecto** del sistema para abrir el **último fichero descargado**.
- [x] Opciones de Preferencias: **ruta temporales**, **crear .m3u**, **límite de velocidad**, **ruta a yt-dlp**.
- [x] Descarga asincrónica (SwingWorker) ejecutando **yt-dlp** con **ProcessBuilder**.
- [x] Estructura Maven/NetBeans con `src/main/resources/{properties,images}`.
- [x] Package raíz **gelabert** y `groupId` en `pom.xml` = **gelabert**.
- [x] `pom.xml` con **maven-compiler-plugin** (release 24) y **maven-jar-plugin** con `Main-Class=gelabert.ytdownloadergui.YTDownloaderGUI`.
- [x] Archivos **.form** de NetBeans para `Mainframe` y `PreferencesPanel`.
