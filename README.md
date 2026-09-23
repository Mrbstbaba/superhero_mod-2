# Superhero Drops Mod (Fabric, Minecraft 1.20.1 Java)

Hayran yapimi bir mod: Marvel/Disney ile resmi bir baglantisi yoktur, sadece kisisel
kullanim icindir. Bir modrinth/curseforge'a yuklemeyi dusunursen isim/marka
kullanimini gozden gecirmen gerekebilir.

## Ne yapiyor?

| Mob | Kosul | Dusen esya | Ihtimal |
|---|---|---|---|
| Demir Golem | oldurulunce | Demir Adam Kostumu | %50 |
| Creeper | **Hulk modu ACIKKEN** oldurulunce | Hulk Gucu | %1 |
| Spider (Orumcek) | oldurulunce | Orumcek Adam Yumurtasi | %1 |
| Spider (Orumcek) | oldurulunce (yumurta dusmediyse de olabilir) | Orumcek Enfeksiyonu (kotu esya) | %5 |

Bir esyayi envanterine aldiginda o gucu "kazanmis" sayilirsin (esya tukense
bile kazanim kalici - bir sonraki sunucu yeniden baslatilana kadar, asagidaki
"Bilinen sinirlar" kismina bak).

## Menu

Varsayilan olarak **INSERT** tusuna basinca (bos duran bir tus) menu acilir.
Menude:
- Iron Man / Hulk / Spiderman modlarini ac-kapat
- Durumunu (hangi gucler kazanildi / aktif) sohbette goster
- Orumcek enfeksiyonunu tedavi et

Tus, oyun ici **Ayarlar > Kontroller > Superkahraman Modu** kisminda degistirilebilir.

## Komutlar (menu olmadan da kullanilabilir)

```
/superhero ironman toggle
/superhero hulk toggle
/superhero spiderman toggle
/superhero status
/superhero cure
```

## Aktifken ne oluyor?

- **Iron Man aktif:** Direnc + Ates Direnci (surekli yenilenir)
- **Hulk aktif:** Guc + Direnc + Can Artisi (buyudugunu simgeler) — ve SADECE
  bu mod acikken oldurulen Creeper'lar Hulk esyasi dusurebilir
- **Spiderman aktif:** Ziplama Guclendirme + Yavas Dusme (orumcek gibi duvara
  tirmanma/agla sallanma su an yok — istersen bir sonraki surumde ekleyebilirim)
- **Enfekte:** periyodik Bulanti + Zayiflik (tedavi olana kadar)

## Kurulum / Derleme

Bu projede Gradle wrapper (gradlew) binary dosyasi yok (internet erisimi
olmadan olusturulamiyor). En kolay yol:

1. https://github.com/FabricMC/fabric-example-mod adresinden 1.20.1 branch'ini indir.
2. O projenin `src` klasorunu bu projenin `src` klasoruyle degistir.
3. `build.gradle`, `gradle.properties`, `settings.gradle` dosyalarini bu
   projedekilerle degistir (veya degerleri karsilastirip elle uyarlat).
4. IntelliJ IDEA'da ac (Fabric gelistirme icin onerilen IDE), Gradle
   senkronize olsun.
5. `./gradlew build` calistir → `build/libs/superheromod-1.0.0.jar` olusur.
6. Bu jar'i, Fabric Loader + Fabric API kurulu bir Minecraft 1.20.1
   `mods` klasorune at.

## Bilinen sinirlar / sonraki adimlar

- Guc verileri su an **bellek-ici** (RAM), sunucu kapanip acilinca sifirlanir.
  Kalicilik istersen NBT/PersistentState ile ekleyebilirim.
- Ikonlar (item textures) basit, elle cizilmis yer tutuculardir; gercekci
  kostum/gorseller icin kendi PNG dosyalarinla `textures/item/` altindakileri
  degistirebilirsin (16x16, item/generated formatinda).
- Spiderman'in ag atma / duvara tirmanma yetenegi henuz yok, istersen ekleyelim.
- Hulk'un fiziksel olarak buyumesi (model degisikligi) yok, sadece efektlerle
  simgeleniyor; gercek model/skin degisimi icin daha ileri bir mixin/renderer
  calismasi gerekir.

Ek istek, degisiklik ya da yeni ozellik (ornegin ag atma, kalici kayit, farkli
ihtimaller, yeni mob'lar) icin soylemen yeterli.
