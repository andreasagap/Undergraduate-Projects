import QtQuick 2.7
import QtQuick.Layouts 1.3
import QtQuick.Dialogs 1.0
import QtQuick.Controls 2.1
import QtMultimedia 5.0
import QtQuick.Dialogs 1.1

Rectangle{
    anchors.fill: parent
    color:"#b3b3b3"

    Component
    {
        id:add
        AddAlarm
        {}
    }



    Component
    {
        id:edit
        EditAlarm
        {}
    }



    ListModel{
        id:myModel
        ListElement {
            img:"on.png"
            hour: "07"
            minutes:"15"
            title_song:""
            day:"Monday"
            status: true
        }

    }

    ToolBar{
        id:bar
        background: Rectangle{color:"#b40000"}
        anchors.top: parent.top
        anchors.left: parent.left
        anchors.right: parent.right
        height: (parent.height)/6
        RowLayout{
            anchors.rightMargin: 0
            anchors.bottomMargin: 0
            anchors.topMargin: 0
            anchors.leftMargin: 10
            spacing: 10
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
                    text:"My Alarms"
                    rightPadding: 0
                    color:"white"
                    clip: false
                    visible: true
                    leftPadding: 0
                    font.bold: false
                    font.pointSize: 10
                }
            }
            ToolButton{
                anchors.fill: (parent)/8
                id:addButton

                Image{

                    anchors.fill:parent
                    source:"add.png"
                    fillMode: Image.PreserveAspectFit
                }
                onClicked: {
                    stack.push(add);
                }
            }
            ToolButton{
                anchors.fill: (parent)/8
                id:delButton
                Image{
                    anchors.fill:parent
                    source:"del.png"
                    fillMode: Image.PreserveAspectFit
                }
                onClicked: mediator.removeAlarm(myView.currentIndex);
            }
        }


}
    GridView {
        id:myView
        cellWidth: bar.width
        cellHeight: (bar.height)/2
        anchors.left: parent.left
        anchors.right: parent.right
        anchors.top: bar.bottom
        anchors.bottom: parent.bottom

            focus: true
            model: mediator.myModel
            delegate: gridDelegate
            highlight: Rectangle{
                width:parent.cellWidth
                height:parent.cellHeight
                radius: 5
                color: "lightgray"
            }

    }

    Component
    {
        id: gridDelegate

        Item{
            width: myView.cellWidth
            height: myView.cellHeight
            ColumnLayout{
                anchors.fill: parent
                RowLayout{
                    spacing: 25
                    CheckBox{
                        id: cb
                        checked: (status === 'true');

                    }

                    Label{
                        id: time
                        text:hour+":"+minutes
                        font.pixelSize:20
                    }
                    Label{
                            text:day
                            font.pixelSize:20
                        }

                }
            }


            MouseArea
            {
                anchors.fill: parent
                onClicked: myView.currentIndex=index
                onDoubleClicked: {
                    var i=myView.currentIndex
                    stack.push(edit, {pHour:mediator.getHour(i),pMinutes:mediator.getMinutes(i),pStatus:mediator.getStatus(i),
                                   psong:mediator.getTitleSong(i),pDay:mediator.getDay(i),pIndex:i})

                }
            }
        }
}
    Item {
        id : clock

        property int hours
        property int minutes
        property string day

        function timeChanged() {
            var date = new Date()
            hours =  date.getHours()
            minutes =date.getMinutes()
            day=date.getDay()
            var days=["Sunday","Monday", "Tuesday", "Wednesday", "Thursday","Friday","Saturday"]
            day=days[day]
        }
    }
    Timer {
        property int ringAlarm:-1
        interval: 7000; running: true; repeat: true;
        onTriggered: {
                clock.timeChanged()
                if(ringAlarm!=-1)
                {
                    if(Number(mediator.getMinutes(ringAlarm))!=Number(clock.minutes))
                    {
                        ringAlarm=-1
                    }
                }
                else{
                    for(var i = 0; i < mediator.count(); i++)
                    {
                        if(mediator.getStatus(i) ==="true" && ((mediator.getDay(i)==="EveryDay") || (mediator.getDay(i)===clock.day)))
                        {
                            if(Number(mediator.getHour(i))==Number(clock.hours) && Number(mediator.getMinutes(i))==Number(clock.minutes))
                            {
                                playMusic.source=mediator.getTitleSong(i);
                                playMusic.play()
                                messageDialog.open()
                                ringAlarm=i
                            }
                        }
                    }
                }
            }

        }
        Audio {
            id: playMusic
            source: ""
            loops:Audio.Infinite
        }
        MessageDialog {
            id: messageDialog
            title: "Stop music"
            icon: StandardIcon.Information
            text: "Wake up"
            standardButtons: StandardButton.Ok
            onAccepted: {
               playMusic.stop()
            }
            Component.onCompleted: visible = false
        }
}






