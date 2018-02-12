import QtQuick 2.7
import QtQuick.Window 2.2
import QtQuick.Controls 2.1
ApplicationWindow {
    id:window
    visible: true
        width: 800
        height: 600
        maximumHeight: 1280
        maximumWidth: 800
        minimumHeight: height
        minimumWidth: width
    title: qsTr("Alarm Clock")

    Component
    {
        id:view;
        AlarmView {}

    }



    StackView
    {
        anchors.fill: parent
        initialItem: view
        id: stack
    }
}
