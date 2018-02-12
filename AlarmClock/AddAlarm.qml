import QtQuick 2.7
import QtQuick.Layouts 1.3
import QtQuick.Controls 1.4
import QtQuick.Dialogs 1.0
import QtQuick.Controls 2.1
import QtQuick.Controls.Styles 1.4
import QtQuick.LocalStorage 2.0


Rectangle{
    id:rec
    anchors.fill: parent
    color:"#b3b3b3"

    FileDialog {
        id: fileDialog
        title: "Please choose a file"
        folder: shortcuts.music
        nameFilters: [ "Music files (*.mp3)"]
        selectFolder:false
        selectMultiple:false
        selectExisting:true
        onAccepted: {
            song_button.text=fileDialog.fileUrl;
            fileDialog.close();
        }
        onRejected: {
            fileDialog.close();
        }
        Component.onCompleted: visible = false
    }



    ToolBar{
        id:bar
        width:parent.width
        background: Rectangle{color:"#b40000"}
        height: (parent.height)/6

        RowLayout{
            anchors.fill: parent
            ColumnLayout{
                  anchors.fill: parent.height
                  Label  {
                      text:"Clock"
                      anchors.leftMargin: 10
                      color:"white"
                      topPadding: 0
                      clip: false
                      visible: true
                      font.bold: true
                      font.pointSize: 30
                  }
                  Label  {
                      text:"Set Alarm"
                      rightPadding: 0
                      color:"white"
                      clip: false
                      visible: true
                      leftPadding: 0
                      font.bold: false
                      font.pointSize: 10
                  }
              }
        }
    }
    ColumnLayout{
        id:column1
        anchors.left: parent.left
        anchors.right: parent.right
        anchors.bottom: parent.bottom
        anchors.top: bar.bottom
        RowLayout{
            id:time_row
            Layout.alignment: Qt.AlignHCenter
            spacing:25

            ColumnLayout{

                Tumbler {
                    id: hour
                    model: 24
                    visibleItemCount: 3
                    font.bold: true
                    font.pointSize: 30
                }
                Text{
                    text: "Hours"
                    leftPadding: 13
                    font{
                        pointSize: 10
                    }
                }
            }
            ColumnLayout{

                Tumbler {
                    id: minutes
                    model: 60
                    visibleItemCount: 3
                    font.bold: true
                    font.pointSize: 30
                }
                Text{
                    text: "Minutes"
                    leftPadding: 11
                    font{
                        pointSize: 10
                    }
                }

            }
        }




            Rectangle{
                color: "transparent"
                Layout.fillHeight: true
                Layout.fillWidth: true
                ColumnLayout{

                    width:parent.width
                    height:parent.height
                    Rectangle{
                        Layout.fillWidth: true
                        height:6
                        color:"#737373"
                    }

                    RowLayout{
                         Button{
                            id:song_button
                            Layout.fillWidth: true

                            text:"Alarm Sound"
                            background: Rectangle{color: "transparent"}
                            onClicked: {fileDialog.open()}
                         }
                    }

                    Rectangle{
                    Layout.fillWidth: true
                    height:6
                    color:"#737373"
                    }
                    ComboBox {
                        id:day
                        width:parent.width
                        model: [ "EveryDay","Monday", "Tuesday", "Wednesday", "Thursday","Friday","Saturday","Sunday" ]
                        background: Rectangle{
                                    color:"transparent"
                                    }
                    }
                    Rectangle{
                        Layout.fillWidth: true
                        height:6
                        color:"#737373"
                    }

                    ComboBox {
                        id:status
                        width:parent.width/2
                        model: [ "Active","Deactive"]
                        background: Rectangle{
                                    color:"transparent"
                                    }
                    }

                }
            }
            Rectangle{
                Layout.fillWidth: true
                height:parent.height/8
                color:"#b3b3b3"
                RowLayout{
                    anchors.fill:parent
                    ToolButton{
                        id:back
                        text:"CANCEL"
                        font.pointSize: 10
                        height:parent.height
                        Layout.fillHeight: true
                        Layout.fillWidth: true
                        width: (parent.width)/2
                        background: Rectangle {
                                    color: button.down ? "#d6d6d6" : "#f6f6f6"
                                    border.color: "#b40000"
                                    border.width: 1

                        }
                        onClicked: {
                          stack.pop();
                        }
                    }
                    Button{
                        id:button
                        text:" DONE "
                        font.pointSize: 10
                        height:parent.height
                        Layout.fillHeight: true
                        Layout.fillWidth: true
                        width:back.width
                        background: Rectangle {
                                    color: button.down ? "#d6d6d6" : "#f6f6f6"
                                    border.color: "#b40000"
                                    border.width: 1

                                }
                        function changeHour()
                        {
                              if (Number(hour.currentIndex) < 10)
                                  return "0"+hour.currentIndex
                              else
                                 return hour.currentIndex

                         }
                        function changeMinutes()
                        {
                              if (Number(minutes.currentIndex) < 10)
                                  return "0"+minutes.currentIndex
                              else
                                 return minutes.currentIndex

                         }
                        onClicked:
                        {
                            var s='false'
                            if(status.currentText=="Active")
                            {
                               s='true'
                            }
                            if(song_button.text.toString()==="Alarm Sound")
                            {
                                song_button.text="sound.mp3"
                            }
                            console.log(song_button.text);
                            mediator.insertAlarm(changeHour(),changeMinutes(),song_button.text.toString(),day.currentText,s.toString());
                            stack.pop()
                        }
                    }
                }
            }
    }
}
