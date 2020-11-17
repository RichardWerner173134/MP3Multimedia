Prototyp zum Erzeugen multimedialer MP3-Präsentationen
==================================

Verwendungszweck:
---------------------
- Dieses Programm dient zum Erstellen multimedialer MP3-Präsentationen. 
- Es verbindet eine MP3-Audiodatei mit beliebig vielen Bilddateien. Am Ende erhält der User eine herkömmliche MP3-Datei, die die ausgewählten Bilder in einem ID3v2-Tag enthält.
- Die MP3-Datei enthält nach Bearbeitung einen SYLT-Frame und mehrere APIC-Frames.
- Die Frames enthalten Bilddaten und Zeitstempel. Diese können vom User extrahiert werden und in einem Wiedergabeprogramm als Präsentation dargestellt werden.

Installation
---------------
- Download des RAR-Archivs unter: https://mega.nz/file/OlJFjSBA#snuUlTJuKarI_C3GSdQDE3XJiWxxHdGxWGtYVI6Dadk
- enthalten sind (Passwort: "testuser"):
    - mp3multimedia.jar
    - README.md
- zum Ausführen des Programms ist lediglich die mp3multimedia.jar auszuführen
- eine funktionierende Java Installation wird vorrausgesetzt

Vorbereitung
----------
* die MP3-Datei muss im Filesystem des Anwenders verfügbar sein 
* die Bilddateien müssen im Filesystem des Anwenders verfügbar sein
* folgende Bildfomate werden aktuell unterstützt: *.png (*.jpg folgt)  

Anleitung
-----------
* Importieren einer MP3

    * Das Herzstück des Editors ist die zu bearbeitende MP3-Datei. Über den Button Neue MP3 kann eine MP3-Datei in die Anwendung importiert werden. Solange keine MP3-Datei geladen ist, bleibt der Wiedergabeplayer ausgegraut. Ebenso bleiben die Buttons Start und Stop ausgegraut. Wird der Button Neue MP3 betätigt, öffnet sich ein Filechooser. Dieser befindet sich immer im Ordner Dokumente des Nutzers. Der Nutzer kann im Filechooser zur gewünschten Datei navigieren. Es können ausschließlich Dateien mit der Endung .mp3 geladen werden. Ist dieser Schritt erfolgreich, schließt sich der Filechooser und der Wiedergabeplayer auf der rechten Seite des Fensters ist ab nun bedienbar. Falls in der MP3-Datei bereits Bilder enthalten sind, so sind diese unter der Playerbar sichtbar und können editiert werden.

$\rightarrow Neue \ MP3 \rightarrow Datei \ im \ Filechooser \ auswählen \rightarrow OK$

![](https://github.com/RichardWerner173134/MP3Multimedia/blob/master/src/main/resources/img/UI/UIMP3geladen.PNG)

* Importieren eines Bildes

    * Beim Start der Anwendung ist die Liste auf der linken Seite der Anwendung leer. Um ein neues Bild zu importieren, muss der Button Bild importieren betätigt werden. Dadurch öffnet sich ein Filechooser. Dort kann der Nutzer zu der gewünschten Datei navigieren. Auch hier wird immer der Ordner Dokumente des Nutzers zuerst geöffnet. Es können lediglich Dateien mit der Dateiendung .jpg oder .png geladen werden. Der Nutzer kann eine oder mehrere Dateien auswählen. Ist der Vorgang erfolgreich, schließt sich der Filechooser und alle ausgewählten Bilder werden der Liste hinzugefügt. Jedes Bild kann nun per Klick ausgewählt werden. Unter der Liste werden Bildname und Bildauflösung sowie eine kleine Vorschau angezeigt. 

$\rightarrow Bild \ importieren \rightarrow Datei(en) \ im \ Filechooser \ auswählen \rightarrow  \ OK$

![](https://github.com/RichardWerner173134/MP3Multimedia/blob/master/src/main/resources/img/UI/UIBildergeladen.PNG)

* Abspielen, Pausieren und Stoppen einer MP3-Datei

    * Ist eine MP3-Datei geladen, ist der Button Start auswählbar. Die Audiodatei kann innerhalb der Anwendung abgespielt werden. Per Klick auf den Button Start wird die Wiedergabe der MP3-Datei gestartet. Der Button Start bekommt ab nun die Funktion Pause. Soll die Wiedergabe auf den Anfang zurückgesetzt werden, muss der Button Stopp betätigt werden.

$\rightarrow Start \rightarrow Pause \rightarrow Start \rightarrow Stop $

* Einbauen eines Bildes

    * Der eigentliche Zweck der Anwendung liegt in der Tätigkeit, Bilder und zugehörige Zeitstempel in der MP3-Datei unterzubringen. Dies wird über den Button Bild einbauen erreicht. Dieser ist nur anwählbar, wenn in der Bilderliste ein Bild ausgewählt ist und eine MP3-Datei geladen ist. Per Klick öffnet sich ein Dialogfenster. Dort wird der Bildname automatisch übernommen und angezeigt. Darunter befinden sich Felder für die Startzeit des Bilder. Die Startzeit für das jeweilige Bild ist im Format Minuten:Sekunden:Millisekunden anzugeben. Die eingegebenen Werte werden validiert. Erlaubt sind lediglich Ziffern. Per Button Cancel kann der  Vorgang abgebrochen werden. Per Button OK wird der Vorgang fortgesetzt. 

$\rightarrow Bild \ aus \ Bilderliste \ auswählen \rightarrow Bild \ einbauen \rightarrow Zeit \ eingeben \rightarrow OK$

![](https://github.com/RichardWerner173134/MP3Multimedia/blob/master/src/main/resources/img/UI/UIDialogEinbauen.png)
	
Ist der Vorgang erfolgreich, wird das Bild auf der rechten Seite des Editors an der entsprechenden Zeit unter der Wiedergabeleiste angezeigt.

![](https://github.com/RichardWerner173134/MP3Multimedia/blob/master/src/main/resources/img/UI/UIvollohneEdit.PNG)

* Einbauen eines Bildes zur aktuellen Wiedergabezeit}

    * Dieser Schritt stellt eine Alternative zum vorherigen Schritt dar. Im Gegensatz zu der vorherigen Methode muss die Startzeit des Bildes nicht manuell angegeben werden. Startet der Nutzer die Wiedergabe der Audiodatei und betätigt im Anschluss den Button Bild einbauen, wird die Wiedergabe angehalten und die aktuelle Wiedergabezeit wird in das Dialogfenster übernommen. 

$\rightarrow Start \rightarrow Bild \ auswählen \rightarrow Bild \ einbauen \rightarrow Zeit \ eingeben \ (automatisch) \rightarrow OK$

* Bearbeiten der Startzeit eines eingebauten Bildes

    * Der Nutzer hat nach Einbauen des Bildes weiterhin die Möglichkeit, die Startzeit zu verändern. Dazu muss der Nutzer auf das entsprechende Bild in der rechten Leiste klicken, um ein Bearbeitungsfenster zu öffnen. Dort wird der Name und die aktuell gültige Startzeit des Bildes angezeigt. Um die Zeit zu korrigieren, kann sie per Hand eingetragen werden. Zum Bestätigen wird auf den Button Aktualisieren geklickt.

$\rightarrow Bild \ aus \ rechtem Balken \ auswählen \rightarrow Zeit \ korrigieren \rightarrow Aktualisieren$

![](https://github.com/RichardWerner173134/MP3Multimedia/blob/master/src/main/resources/img/UI/UIvollmitEdit.png)

* Löschen eines eingebauten Bildes}

    * Der Nutzer hat außerdem die Möglichkeit, ein bereits eingebautes Bild wieder zu entfernen. Dazu muss ebenfalls das Bild in der Leiste rechts ausgewählt werden, um das Bearbeitungsfenster zu öffnen. Dort befindet sich ein weiterer Button Entfernen. Wird dieser betätigt, wird das Bild und seine Zeitstempel wieder entfernt.

$\rightarrow Bild \ aus \ rechtem \ Balken \ auswählen \rightarrow Entfernen$

* Speichern der Datei

    * Die MP3-Datei soll nach erfolgreicher Bearbeitung gespeichert werden. Dazu muss im Datei-Menü Speichern ausgewählt werden. Dadurch öffnet sich ein Filechooser. Hier hat der Nutzer zwei verschiedene Möglichkeiten, die Datei zu speichern. Der Nutzer kann die originale MP3-Datei überschreiben oder er kann eine neue MP3-Datei erstellen lassen. Um die originale Datei zu überschreiben, muss der Nutzer diese im Filechooser auswählen und auf Open klicken. Möchte der Nutzer seine Ergebnisse in einer neuen MP3-Datei abspeichern, muss er im Filechooser zum gewünschten Pfad navigieren und einen Dateinamen eingeben. Im Anschluss bestätigt er den Vorgang mit Open. Ist der Vorgang erfolgreich, erhält der Nutzer ein Feedbackfenster und kehrt zum Hauptfenster des Editors zurück. Das Datei-Menü ist in Abbildung \ref{UIMenu} zu sehen.

![](https://github.com/RichardWerner173134/MP3Multimedia/blob/master/src/main/resources/img/UI-MenuSkaliert.png)

$\rightarrow Menü: \ Datei \rightarrow Menu-Eintrag: \ Speichern \rightarrow Dateipfad \ und \ Dateinamen \ angeben \rightarrow OK$

* Zurücksetzen des Arbeitsplatzes

    * Möchte der Nutzer seine bisher erreichten Bearbeitungsergebnisse verwerfen, kann er dies durch das Zurücksetzen des Arbeitsplatzes erreichen. Die Funktionalität ist im Dateimenü zu finden (siehe Abbildung \ref{UIMenu}). Dort gibt es einen Eintrag Arbeitsplatz zurücksetzen. Wird dies getan, werden alle Einträge der Bilderliste, die geladene MP3-Datei und alle eingebauten Bilder entfernt. Es ist eine völlig leere Instanz zu sehen.

$\rightarrow Menü: \ Datei \rightarrow Menü-Eintrag: \ Arbeitsplatz \ zurücksetzen$

Wiedergabe
-----------
* Wiedergabeprogramme müssen folgendes beachten:
    * beim Exportvorgang werden die Frames in einen ID3-Tag der Version ID3v2.4 geschrieben
    * die Frames gehören zur ID3v2.4-Version
    * zum Speichern der Informationen werden mehrere APIC-Frames und ein SYLT-Frame verwendet
    * die APIC-Frames enthalten die Bilddaten
        * Textencoding: UTF-8
        * MIME-Type: image/png, image/jpeg
        * Picture-Type: Other
        * Description: \<Name der Bilddatei.Endung\>
        * Data: Bilddaten
    * der SYLT-Frame enthält die Information, zu welcher Zeit ein Bild aus einem APIC-Frame angezeigt wird (Zeitstempel)
        * Textencoding: UTF-8
        * Language: eng
        * TimestampFormat: Startzeit des Bildes in Millisekunden seit Beginn der Audiodatei
        * Content-Type: URL to images
        * Description: bleibt leer
        * Data: 
            * \<Name der Bilddatei.Endung\>\<Synchronisationsbyte\>\<Timestamp\>\<Name der Bilddatei.Endung\>\<Synchronisationsbyte\>\<Timestamp\>...
            * Dabei ist folgendes zu beachten:
                * \<Name der Bilddatei.Endung\> kodiert in ${Textencoding}
                * \<Synchronisationsbyte\> 1 Byte -- 0x00
                * \<Timestamp\> 4 Bytes -- 0x00000000
                * Beispiel: 
                
![](https://github.com/RichardWerner173134/MP3Multimedia/blob/master/src/main/resources/img/SYLT-Framebody-values.png).

Bildname        | Synchronisationsbyte  | Timestamp hexadezimal | Timestamp dezimal 
----------------|-----------------------|-----------------------|-------------------
imageFile1.png  | 0x00                  | 0x00000000            | 0s
imageFile2.png  | 0x00                  | 0x00007530            | 30s
imageFile3.jpg  | 0x00                  | 0x0001D4C0            | 2min = 120s
imageFile1.png  | 0x00                  | 0x000927C0            | 10min = 600000s	

* Folgendes Programm ermöglicht bereits die Wiedergabe einer solchen Multimedia-Präsentationen:
https://www.microsoft.com/de-de/p/mp3-multimedia/9nt7d443vx86

Quellenangabe
--------------

* M. Nilsson, ID3v24: https://id3.org/id3v2.4.0-frames
* M. Nilsson, ID3v23: https://id3.org/id3v2.3.0
* Jens Grätzer, MP3 Multimedia: https://www.microsoft.com/de-de/p/mp3-multimedia/9nt7d443vx86
* Richard Werner, Prototyp eines Editors zum Erzeugen multimedialer MP3-Präsentationen: Teile dieses ReadmeFiles sind meiner eigenen Bachelorarbeit "Prototyp eines Editors zum Erzeugen multimedialer MP3-Präsentationen" entnommen
