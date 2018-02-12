#ifndef MEDIATOR_H
#define MEDIATOR_H

#include <QObject>
#include"alarmmodel.h"

class Mediator : public QObject {
    Alarmmodel *_AlarmModel;
    Q_OBJECT
    Q_PROPERTY(Alarmmodel* myModel READ myModel WRITE setAlarmModel NOTIFY AlarmModelChanged)
    QString path;
public:
    explicit Mediator(QObject *parent = 0);
    void setAlarmModel(Alarmmodel* m)
    {
        _AlarmModel = m;
        emit AlarmModelChanged();
    }
    Alarmmodel* myModel(){
        return _AlarmModel;
    }
    void updateFile();
signals:
    void AlarmModelChanged();
public slots:
    void insertAlarm(QString hour,QString minutes,QString title_song,QString day,QString status);
    void removeAlarm(int index);
    int count();
    QString getHour(int i);
    QString getMinutes(int i);
    QString getTitleSong(int i);
    QString getDay(int i);
    QString getStatus(int i);
    void setStatus(int i, QString s);
};

#endif // MEDIATOR_H
