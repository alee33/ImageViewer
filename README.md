ImageViewer
===========
- выбор источника данных для слайд-шоу - внутренняя память или SD-карта или социальная сеть 
- контент должен быть во весь экран 
- необходимо хранить следующие настройки приложения в БД:
 - время запуска приложения (тип - time)
 - время остановки приложения (тип - time)
 - длина задержки между кадрами (тип - integer, в секундах, от 1 сек. до 24 часов)
 - автоматический запуск во время зарядки (тип - boolean, да/нет)
 - автоматический запуск после перезагрузки устройства (тип - boolean, да/нет)
- контент должен быть во весь экран, в том числе в области Navigation Bar
- возможность хранить профили приложения (т.е. различные наборы настроек)
- блокировка экрана на время слайд-шоу (в том числе кнопок из Navigation Bar), разблокировка по комбинации нажатий - например, 3 быстрых нажатия
- хотя бы пара эффектов смены кадров - например, "fade in/fade out" и "from left to right"
