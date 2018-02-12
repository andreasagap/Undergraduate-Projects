/****************************************************************************
** Meta object code from reading C++ file 'mediator.h'
**
** Created by: The Qt Meta Object Compiler version 67 (Qt 5.8.0)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../mediator.h"
#include <QtCore/qbytearray.h>
#include <QtCore/qmetatype.h>
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'mediator.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 67
#error "This file was generated using the moc from 5.8.0. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
QT_WARNING_PUSH
QT_WARNING_DISABLE_DEPRECATED
struct qt_meta_stringdata_Mediator_t {
    QByteArrayData data[25];
    char stringdata0[200];
};
#define QT_MOC_LITERAL(idx, ofs, len) \
    Q_STATIC_BYTE_ARRAY_DATA_HEADER_INITIALIZER_WITH_OFFSET(len, \
    qptrdiff(offsetof(qt_meta_stringdata_Mediator_t, stringdata0) + ofs \
        - idx * sizeof(QByteArrayData)) \
    )
static const qt_meta_stringdata_Mediator_t qt_meta_stringdata_Mediator = {
    {
QT_MOC_LITERAL(0, 0, 8), // "Mediator"
QT_MOC_LITERAL(1, 9, 17), // "AlarmModelChanged"
QT_MOC_LITERAL(2, 27, 0), // ""
QT_MOC_LITERAL(3, 28, 11), // "insertAlarm"
QT_MOC_LITERAL(4, 40, 3), // "img"
QT_MOC_LITERAL(5, 44, 4), // "hour"
QT_MOC_LITERAL(6, 49, 7), // "minutes"
QT_MOC_LITERAL(7, 57, 10), // "title_song"
QT_MOC_LITERAL(8, 68, 3), // "day"
QT_MOC_LITERAL(9, 72, 6), // "status"
QT_MOC_LITERAL(10, 79, 11), // "removeAlarm"
QT_MOC_LITERAL(11, 91, 5), // "index"
QT_MOC_LITERAL(12, 97, 5), // "count"
QT_MOC_LITERAL(13, 103, 6), // "getImg"
QT_MOC_LITERAL(14, 110, 1), // "i"
QT_MOC_LITERAL(15, 112, 7), // "getHour"
QT_MOC_LITERAL(16, 120, 10), // "getMinutes"
QT_MOC_LITERAL(17, 131, 12), // "getTitleSong"
QT_MOC_LITERAL(18, 144, 6), // "getDay"
QT_MOC_LITERAL(19, 151, 9), // "getStatus"
QT_MOC_LITERAL(20, 161, 9), // "setStatus"
QT_MOC_LITERAL(21, 171, 1), // "s"
QT_MOC_LITERAL(22, 173, 6), // "setImg"
QT_MOC_LITERAL(23, 180, 7), // "myModel"
QT_MOC_LITERAL(24, 188, 11) // "Alarmmodel*"

    },
    "Mediator\0AlarmModelChanged\0\0insertAlarm\0"
    "img\0hour\0minutes\0title_song\0day\0status\0"
    "removeAlarm\0index\0count\0getImg\0i\0"
    "getHour\0getMinutes\0getTitleSong\0getDay\0"
    "getStatus\0setStatus\0s\0setImg\0myModel\0"
    "Alarmmodel*"
};
#undef QT_MOC_LITERAL

static const uint qt_meta_data_Mediator[] = {

 // content:
       7,       // revision
       0,       // classname
       0,    0, // classinfo
      12,   14, // methods
       1,  120, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       1,       // signalCount

 // signals: name, argc, parameters, tag, flags
       1,    0,   74,    2, 0x06 /* Public */,

 // slots: name, argc, parameters, tag, flags
       3,    6,   75,    2, 0x0a /* Public */,
      10,    1,   88,    2, 0x0a /* Public */,
      12,    0,   91,    2, 0x0a /* Public */,
      13,    1,   92,    2, 0x0a /* Public */,
      15,    1,   95,    2, 0x0a /* Public */,
      16,    1,   98,    2, 0x0a /* Public */,
      17,    1,  101,    2, 0x0a /* Public */,
      18,    1,  104,    2, 0x0a /* Public */,
      19,    1,  107,    2, 0x0a /* Public */,
      20,    2,  110,    2, 0x0a /* Public */,
      22,    2,  115,    2, 0x0a /* Public */,

 // signals: parameters
    QMetaType::Void,

 // slots: parameters
    QMetaType::Void, QMetaType::QString, QMetaType::QString, QMetaType::QString, QMetaType::QString, QMetaType::QString, QMetaType::QString,    4,    5,    6,    7,    8,    9,
    QMetaType::Void, QMetaType::Int,   11,
    QMetaType::Int,
    QMetaType::QString, QMetaType::Int,   14,
    QMetaType::QString, QMetaType::Int,   14,
    QMetaType::QString, QMetaType::Int,   14,
    QMetaType::QString, QMetaType::Int,   14,
    QMetaType::QString, QMetaType::Int,   14,
    QMetaType::QString, QMetaType::Int,   14,
    QMetaType::Void, QMetaType::Int, QMetaType::QString,   14,   21,
    QMetaType::Void, QMetaType::Int, QMetaType::QString,   14,    4,

 // properties: name, type, flags
      23, 0x80000000 | 24, 0x0049500b,

 // properties: notify_signal_id
       0,

       0        // eod
};

void Mediator::qt_static_metacall(QObject *_o, QMetaObject::Call _c, int _id, void **_a)
{
    if (_c == QMetaObject::InvokeMetaMethod) {
        Mediator *_t = static_cast<Mediator *>(_o);
        Q_UNUSED(_t)
        switch (_id) {
        case 0: _t->AlarmModelChanged(); break;
        case 1: _t->insertAlarm((*reinterpret_cast< QString(*)>(_a[1])),(*reinterpret_cast< QString(*)>(_a[2])),(*reinterpret_cast< QString(*)>(_a[3])),(*reinterpret_cast< QString(*)>(_a[4])),(*reinterpret_cast< QString(*)>(_a[5])),(*reinterpret_cast< QString(*)>(_a[6]))); break;
        case 2: _t->removeAlarm((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 3: { int _r = _t->count();
            if (_a[0]) *reinterpret_cast< int*>(_a[0]) = _r; }  break;
        case 4: { QString _r = _t->getImg((*reinterpret_cast< int(*)>(_a[1])));
            if (_a[0]) *reinterpret_cast< QString*>(_a[0]) = _r; }  break;
        case 5: { QString _r = _t->getHour((*reinterpret_cast< int(*)>(_a[1])));
            if (_a[0]) *reinterpret_cast< QString*>(_a[0]) = _r; }  break;
        case 6: { QString _r = _t->getMinutes((*reinterpret_cast< int(*)>(_a[1])));
            if (_a[0]) *reinterpret_cast< QString*>(_a[0]) = _r; }  break;
        case 7: { QString _r = _t->getTitleSong((*reinterpret_cast< int(*)>(_a[1])));
            if (_a[0]) *reinterpret_cast< QString*>(_a[0]) = _r; }  break;
        case 8: { QString _r = _t->getDay((*reinterpret_cast< int(*)>(_a[1])));
            if (_a[0]) *reinterpret_cast< QString*>(_a[0]) = _r; }  break;
        case 9: { QString _r = _t->getStatus((*reinterpret_cast< int(*)>(_a[1])));
            if (_a[0]) *reinterpret_cast< QString*>(_a[0]) = _r; }  break;
        case 10: _t->setStatus((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< QString(*)>(_a[2]))); break;
        case 11: _t->setImg((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< QString(*)>(_a[2]))); break;
        default: ;
        }
    } else if (_c == QMetaObject::IndexOfMethod) {
        int *result = reinterpret_cast<int *>(_a[0]);
        void **func = reinterpret_cast<void **>(_a[1]);
        {
            typedef void (Mediator::*_t)();
            if (*reinterpret_cast<_t *>(func) == static_cast<_t>(&Mediator::AlarmModelChanged)) {
                *result = 0;
                return;
            }
        }
    } else if (_c == QMetaObject::RegisterPropertyMetaType) {
        switch (_id) {
        default: *reinterpret_cast<int*>(_a[0]) = -1; break;
        case 0:
            *reinterpret_cast<int*>(_a[0]) = qRegisterMetaType< Alarmmodel* >(); break;
        }
    }

#ifndef QT_NO_PROPERTIES
    else if (_c == QMetaObject::ReadProperty) {
        Mediator *_t = static_cast<Mediator *>(_o);
        Q_UNUSED(_t)
        void *_v = _a[0];
        switch (_id) {
        case 0: *reinterpret_cast< Alarmmodel**>(_v) = _t->myModel(); break;
        default: break;
        }
    } else if (_c == QMetaObject::WriteProperty) {
        Mediator *_t = static_cast<Mediator *>(_o);
        Q_UNUSED(_t)
        void *_v = _a[0];
        switch (_id) {
        case 0: _t->setAlarmModel(*reinterpret_cast< Alarmmodel**>(_v)); break;
        default: break;
        }
    } else if (_c == QMetaObject::ResetProperty) {
    }
#endif // QT_NO_PROPERTIES
}

const QMetaObject Mediator::staticMetaObject = {
    { &QObject::staticMetaObject, qt_meta_stringdata_Mediator.data,
      qt_meta_data_Mediator,  qt_static_metacall, Q_NULLPTR, Q_NULLPTR}
};


const QMetaObject *Mediator::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->dynamicMetaObject() : &staticMetaObject;
}

void *Mediator::qt_metacast(const char *_clname)
{
    if (!_clname) return Q_NULLPTR;
    if (!strcmp(_clname, qt_meta_stringdata_Mediator.stringdata0))
        return static_cast<void*>(const_cast< Mediator*>(this));
    return QObject::qt_metacast(_clname);
}

int Mediator::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QObject::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        if (_id < 12)
            qt_static_metacall(this, _c, _id, _a);
        _id -= 12;
    } else if (_c == QMetaObject::RegisterMethodArgumentMetaType) {
        if (_id < 12)
            *reinterpret_cast<int*>(_a[0]) = -1;
        _id -= 12;
    }
#ifndef QT_NO_PROPERTIES
   else if (_c == QMetaObject::ReadProperty || _c == QMetaObject::WriteProperty
            || _c == QMetaObject::ResetProperty || _c == QMetaObject::RegisterPropertyMetaType) {
        qt_static_metacall(this, _c, _id, _a);
        _id -= 1;
    } else if (_c == QMetaObject::QueryPropertyDesignable) {
        _id -= 1;
    } else if (_c == QMetaObject::QueryPropertyScriptable) {
        _id -= 1;
    } else if (_c == QMetaObject::QueryPropertyStored) {
        _id -= 1;
    } else if (_c == QMetaObject::QueryPropertyEditable) {
        _id -= 1;
    } else if (_c == QMetaObject::QueryPropertyUser) {
        _id -= 1;
    }
#endif // QT_NO_PROPERTIES
    return _id;
}

// SIGNAL 0
void Mediator::AlarmModelChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 0, Q_NULLPTR);
}
QT_WARNING_POP
QT_END_MOC_NAMESPACE
