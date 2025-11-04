package kz.talipovsn.weather12;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MainActivity extends AppCompatActivity  {


    private MapView mapView; // Компонент карты

    private final int PERSMISSION_ALL = 1; // Код запроса разрешений

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_main); // Устанавливаем макет
        } catch (Exception ignore) {
        }

        // Установка темы
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        // Требуемые разрешения для Android >= 6
        String[] PERMISSIONS = { // Массив разрешений
                Manifest.permission.WRITE_EXTERNAL_STORAGE, // Разрешение на запись во внешнее хранилище
                Manifest.permission.ACCESS_FINE_LOCATION, // Разрешение на доступ к точному местоположению
                Manifest.permission.READ_EXTERNAL_STORAGE, // Разрешение на чтение из внешнего хранилища
                Manifest.permission.INTERNET, // Разрешение на доступ к интернету
                Manifest.permission.ACCESS_NETWORK_STATE // Разрешение на доступ к состоянию сети
        };

        // Запрос разрешений, если их еще нет для Android >= 6
        if (!hasPermissions(this, PERMISSIONS)) { // Если нет разрешений
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERSMISSION_ALL); // Запрашиваем разрешения
        }

        // Подключение карт OSM
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        // Инициализация компонента карты
        mapView = this.findViewById(R.id.mapview); // Инициализация компонента карты
        mapView.setBuiltInZoomControls(true); // Разрешаем масштабирование карты
        mapView.setMultiTouchControls(true); // Разрешаем мультитач карты
        mapView.setTileSource(TileSourceFactory.MAPNIK); // Устанавливаем источник тайлов (картинок) карты

        // Инициализация контроллера карты
        IMapController mapController = mapView.getController(); // Получаем контроллер карты
        mapController.setZoom(11.0); // Устанавливаем начальное масштабирование карты
        mapController.setCenter(new GeoPoint(52.2772, 76.9865)); // Устанавливаем начальную позицию карты на центр города
        mapController.stopPanning(); // Останавливаем панорамирование карты (перемещение карты)

        // Установка вращения карты
        final DisplayMetrics dm = this.getResources().getDisplayMetrics(); // Получаем метрики экрана
        RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(mapView); // Создаем компонент вращения карты
        mRotationGestureOverlay.setEnabled(true); // Разрешаем вращение компонента карты
        mapView.getOverlays().add(mRotationGestureOverlay); // Добавляем компонент вращения карты

        // Установка масштабной линейки на карту
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(mapView); // Создаем компонент линейки масштаба
        mScaleBarOverlay.setScaleBarOffset(0, (int) (40 * dm.density)); // Устанавливает смещение экрана шкалы для линейки
        mScaleBarOverlay.setCentred(true); // Устанавливаем центрирование масштаба
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10); // Устанавливаем отступ масштаба
        mapView.getOverlays().add(mScaleBarOverlay); // Добавляем компонент масштабирования карты

        // Разрешаем определять текущую позицию
        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        mLocationOverlay.enableMyLocation(); // Разрешаем определение текущей позиции
        mapView.getOverlays().add(mLocationOverlay); // Добавляем компонент определения текущей позиции на карту

        // Разрешаем компас на карте
        CompassOverlay mCompassOverlay = new CompassOverlay(this, new InternalCompassOrientationProvider(this), mapView);
        mCompassOverlay.setPointerMode(true); // Устанавливаем режим указателя
        mCompassOverlay.enableCompass(); // Разрешаем компас
        mapView.getOverlays().add(mCompassOverlay); // Добавляем компас на карту

        // Устанавливаем маркер 1 на карту
        Marker startMarker = new Marker(mapView); // Создаем маркер
        startMarker.setPosition(new GeoPoint(52.2561941, 77.0422887)); // Устанавливаем позицию маркера
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM); // Устанавливаем якорь маркера
        startMarker.setTitle("Алюминиевый завод"); // Устанавливаем заголовок маркера
        mapView.getOverlays().add(startMarker); // Добавляем маркер на карту

        // Устанавливаем маркер 2 на карту
        Marker startMarker2 = new Marker(mapView); // Создаем маркер
        startMarker2.setPosition(new GeoPoint(52.3699894, 76.9072292)); // Устанавливаем позицию маркера
        startMarker2.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM); // Устанавливаем якорь маркера
        startMarker2.setTitle("ПНХЗ"); // Устанавливаем заголовок маркера
        mapView.getOverlays().add(startMarker2); // Добавляем маркер на карту

    }

    // Действия проверки нужных разрешений для Android >= 6
    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) { // Если контекст и разрешения не пустые
            for (String permission : permissions) { // Перебираем разрешения
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false; // Если разрешения не получены, возвращаем false
                }
            }
        }
        return true; // Если разрешения получены, возвращаем true
    }

    // Действия при окончании установки разрешений для Android >= 6
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERSMISSION_ALL) { // Если запрос разрешений
            for (int  result : grantResults)  // Перебираем результаты запроса
            if (result != PackageManager.PERMISSION_GRANTED) { // Если разрешения не получены
                return; // Выходим из метода
            }
            reload(); // Перезагружаем окно
        }
    }

    // Перезагрузка окна
    private void reload() {
        Intent intent = getIntent(); // Получаем намерение
        overridePendingTransition(0, 0); // Отключаем анимацию
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); // Добавляем флаг отключения анимации
        finish(); // Закрываем окно
        overridePendingTransition(0, 0); // Отключаем анимацию
        startActivity(intent); // Запускаем окно программы заново
    }

    // Создание меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Обработчик меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.about) { // Пункт меню "О программе"
            Toast toast = Toast.makeText(getApplicationContext(), R.string.email, Toast.LENGTH_LONG);
            toast.show();
            return true;
        }

        if (id == R.id.exit) { // Пункт меню "Выход"
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item); // Вызываем суперкласс для обработки остальных пунктов меню
    }


}
