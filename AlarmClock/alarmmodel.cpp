#include "alarmmodel.h"

Alarmmodel::Alarmmodel(QObject *parent): QAbstractListModel(parent) {
}
void Alarmmodel::addAlarm(const Alarm &alarm)
{
    beginInsertRows(QModelIndex(), rowCount(), rowCount());
    m_alarms << alarm;
    endInsertRows();
}

void Alarmmodel::removeAlarm (int index)
{
    beginRemoveRows(QModelIndex(), index, index);
    m_alarms.removeAt(index);
    endRemoveRows();
}

int Alarmmodel::rowCount(const QModelIndex & parent) const
{
    Q_UNUSED(parent);
    return m_alarms.count();
}
QVariant Alarmmodel::data(const QModelIndex &index, int role) const
{
    if (index.row() < 0 || index.row() >= m_alarms.count())
        return QVariant();
    const Alarm &alarm = m_alarms[index.row()];
    if (role == HourRole)
        return alarm.getHour();
    else if (role == MinuteRole)
        return alarm.getMinutes();
    else if (role == DayRole)
        return alarm.getDay();
    else if (role == TitleSongRole)
        return alarm.getTitleSong();
    else if (role == StatusRole)
        return alarm.getStatus();
    return QVariant();
}
QHash<int, QByteArray> Alarmmodel::roleNames() const {
    QHash<int, QByteArray> roles;
    roles[HourRole] = "hour";
    roles[MinuteRole] = "minutes";
    roles[DayRole] = "day";
    roles[TitleSongRole] = "title_song";
    roles[StatusRole] = "status";
    return roles;
}
QString Alarmmodel::getHour(int i)
{
    if (i>=0 && i<m_alarms.count())
        return m_alarms[i].getHour();
    return NULL;
}
QString Alarmmodel::getMinutes(int i)
{
    if (i>=0 && i<m_alarms.count())
        return m_alarms[i].getMinutes();
    return NULL;
}

QString Alarmmodel::getTitleSong(int i)
{
    if (i>=0 && i<m_alarms.count())
        return m_alarms[i].getTitleSong();
    return NULL;
}

QString Alarmmodel::getDay(int i){
    if (i>=0 && i<m_alarms.count())
        return m_alarms[i].getDay();
    return NULL;
}

QString Alarmmodel::getStatus(int i)
{
    if (i>=0 && i<m_alarms.count())
        return m_alarms[i].getStatus();
    return NULL;
}
void Alarmmodel::setStatus(int i,QString s)
{
    if (i>=0 && i<m_alarms.count())
       m_alarms[i].setStatus(s);

}
