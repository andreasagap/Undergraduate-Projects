#include <QFile>
#include <QStandardPaths>
#include <QDir>
#include <QTextStream>
#include "mediator.h"

Mediator::Mediator(QObject *parent) : QObject(parent)
{
    _AlarmModel = new Alarmmodel();
    path = QStandardPaths::standardLocations(QStandardPaths::DataLocation).value(0);
    QDir dir(path);
    if (!dir.exists())
        dir.mkpath(path);
    if (!path.isEmpty() && !path.endsWith("/"))
        path += "/";
    path+="alarms.txt";

    qDebug(path.toLatin1());

    QFile file(path);
    if (file.open(QIODevice::ReadOnly | QIODevice::Text))
    {
        QTextStream in(&file);
        while (!in.atEnd())
        {
            QString hour,minutes,title_song,day,status;
            hour=in.readLine();
            minutes=in.readLine();
            title_song=in.readLine();
            day=in.readLine();
            status=in.readLine();
            if(hour.length()==0)
            {
                break;
            }
            _AlarmModel->addAlarm(Alarm(hour,minutes,title_song,day,status));
        }
        file.close();
    }
}
void Mediator::updateFile()
{
    int i;

    QFile file(path);
    int rCount = _AlarmModel->rowCount();
    if (file.open(QIODevice::WriteOnly | QIODevice::Text))
    {
        QTextStream out(&file);
        for (i=0;i< rCount;i++){
            out<<_AlarmModel->getHour(i)<<endl<< _AlarmModel->getMinutes(i)<<endl<< _AlarmModel->getTitleSong(i)<<endl<< _AlarmModel->getDay(i)<<endl<< _AlarmModel->getStatus(i)<<endl;
        }

        file.close();
    }
}
int Mediator::count()
{
    return _AlarmModel->rowCount();
}

void Mediator::removeAlarm(int index){
    _AlarmModel->removeAlarm(index);
    AlarmModelChanged();
    updateFile();
}

void Mediator::insertAlarm(QString hour,QString minutes,QString title_song,QString day,QString status){
    _AlarmModel->addAlarm(Alarm(hour,minutes,title_song,day, status));
    AlarmModelChanged();
    updateFile();
}
QString Mediator::getHour(int i)
{
    return _AlarmModel->getHour(i);
}

QString Mediator::getMinutes(int i)
{
    return _AlarmModel->getMinutes(i);
}

QString Mediator::getTitleSong(int i){
    return _AlarmModel->getTitleSong(i);
}

QString Mediator::getDay(int i){
    return _AlarmModel->getDay(i);
}

QString Mediator::getStatus(int i){
    return _AlarmModel->getStatus(i);
}
void Mediator::setStatus(int i, QString s)
{
    _AlarmModel->setStatus(i,s);
    AlarmModelChanged();
    updateFile();
}

