#include "alarm.h"

Alarm::Alarm(const QString &hour,const QString &minutes,const QString &title_song,const QString &day,const QString &status)
{
    m_hour=hour;
    m_minutes=minutes;
    m_title_song=title_song;
    m_day=day;
    m_status=status;
}
QString Alarm::getHour() const
{
    return m_hour;
}
void Alarm::setHour(const QString &hour)
{
    m_hour=hour;
}

QString Alarm::getMinutes() const
{
    return m_minutes;
}
void Alarm::setMinutes(const QString &minutes)
{
    m_minutes=minutes;
}

QString Alarm::getTitleSong() const
{
    return m_title_song;
}
void Alarm::setTitleSong(const QString &title_song)
{
     m_title_song=title_song;
}

QString Alarm::getDay() const
{
    return m_day;
}
void Alarm::setDay(const QString &day)
{
    m_day=day;
}

QString Alarm::getStatus() const
{
    return m_status;
}
void Alarm::setStatus(const QString &status)
{
     m_status=status;
}
