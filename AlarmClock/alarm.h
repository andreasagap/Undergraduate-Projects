#ifndef ALARM_H
#define ALARM_H
#include <QString>

class Alarm
{
public:
    Alarm(const QString &hour,const QString &minutes,const QString &title_song,const QString &day,const QString &status);
    QString getHour() const;
    void setHour(const QString &hour);
    QString getMinutes() const;
    void setMinutes(const QString &minutes);
    QString getTitleSong() const;
    void setTitleSong(const QString &title_song);
    QString getDay() const;
    void setDay(const QString &day);
    QString getStatus() const;
    void setStatus(const QString &status);
private:
    QString m_hour;
    QString m_minutes;
    QString m_title_song;
    QString m_day;
    QString m_status;
};

#endif // ALARM_H
