#include <QGuiApplication>
#include <QQmlApplicationEngine>
#include <QQmlContext>
#include "mediator.h"

int main(int argc, char *argv[])
{
    QGuiApplication::setAttribute(Qt::AA_EnableHighDpiScaling);
    QGuiApplication app(argc, argv);

    Mediator *m = new Mediator();

    QQmlApplicationEngine engine;
    engine.rootContext()->setContextProperty("mediator",m);
    engine.load(QUrl(QStringLiteral("qrc:/main.qml")));
    return app.exec();
}
