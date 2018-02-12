#ifndef ALARMMODEL_H
#define ALARMMODEL_H
#include <QObject>
#include <QAbstractListModel>
#include "alarm.h"

class Alarmmodel: public QAbstractListModel
{
    Q_OBJECT
    public:
        enum AlarmRoles {
            HourRole,
            DayRole,
            MinuteRole,StatusRole,TitleSongRole
        };
        Alarmmodel(QObject *parent = 0);
        void addAlarm(const Alarm &alarm);
        void removeAlarm (int index);
        int rowCount(const QModelIndex & parent = QModelIndex()) const;
        QVariant data(const QModelIndex &index, int role=Qt::DisplayRole) const;
        QString getHour(int i);
        QString getMinutes(int i);
        QString getTitleSong(int i);
        QString getDay(int i);
        QString getStatus(int i);
        void setStatus(int i,QString s);
    protected:
        QHash<int, QByteArray> roleNames() const;
    private:
        QList<Alarm> m_alarms;
};

#endif // ALARMMODEL_H
