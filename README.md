Conversor de Monedas (Java 17 + Maven + HttpClient + Gson)

Aplicación de consola que convierte entre monedas usando:

ExchangeRate-API (requiere EXCHANGE_API_KEY)

Frankfurter como fallback 100% gratis (sin clave).

Incluye menú en bucle, validaciones de entrada, y parseo JSON con Gson.

Probado con Java 17 (Temurin) e IntelliJ IDEA en Windows.


✨ Características

Cliente HTTP con java.net.http.HttpClient (Java 11+).

Dos proveedores:

ExchangeRate-API → conversion_rate (si defines EXCHANGE_API_KEY).

Frankfurter → rates[TO] (si no hay key, sin registro).

Parseo JSON con Gson 2.10.1.

Menú interactivo con 6 conversiones frecuentes:

USD ↔ ARS, USD ↔ BRL, USD ↔ CLP

Validaciones de:

Opción de menú (0–6)

Monto numérico > 0

Códigos ISO de 3 letras (si se usara modo libre)

Mensajes de error claros (HTTP, formato, entradas inválidas).


🧰 Stack técnico

Java 17 (Temurin recomendado)

Maven 3.x

Gson 2.10.1

IntelliJ IDEA (Community) – opcional pero recomendado


✅ Requisitos

Java 11+ (recomendado 17)

Maven 3.x

(Opcional) Clave de ExchangeRate-API
Si no la defines, el app usa Frankfurter automáticamente.


📦 Instalación y build

Clona o copia el proyecto, luego:

# Windows / Linux / macOS
mvn clean package


Debes ver BUILD SUCCESS y el artefacto:

target/conversor-monedas-1.0-SNAPSHOT.jar


Si el arquetipo trajo tests: ya está JUnit 4 agregado.
Para empaquetar más rápido: mvn -DskipTests clean package


🔑 Configurar la API Key (opcional)
IntelliJ (recomendado)

Run → Edit Configurations…

Selecciona tu Application (com.jrm.conversor.App)

Environment variables → EXCHANGE_API_KEY=TU_API_KEY → OK

Ejecuta ▶️

PowerShell (usuario actual)
setx EXCHANGE_API_KEY "TU_API_KEY"
# reinicia IntelliJ para que la vea


Si no defines la key, el cliente usa Frankfurter (gratis, sin registro).

▶️ Ejecutar
Desde IntelliJ

Abre src/main/java/com/jrm/conversor/App.java

Run ▶️

Asegúrate que la Run Configuration use el classpath del módulo conversor-monedas.

Desde consola (con jar “sombreado”) – ver sección “Jar ejecutable”
java -jar target/conversor-monedas-1.0-SNAPSHOT-shaded.jar


🕹️ Uso (menú)

Ejemplo de sesión:

=== Conversor de Monedas ===
1) USD  -> ARS
2) ARS  -> USD
3) USD  -> BRL
4) BRL  -> USD
5) USD  -> CLP
6) CLP  -> USD
0) Salir
Elija una opción: 3
Monto a convertir: 500
[DEBUG] Provider=ExchangeRate-API URL=https://v6.exchangerate-api.com/v6/****/pair/USD/BRL
500.00 USD  x  5.443500 (BRL/USD)  =  2,721.75 BRL


0 para salir.

Entradas fuera de 0–6 o montos no numéricos → mensaje y reintento.

Códigos de moneda inválidos → mensaje y reintento (a nivel de cliente).


🗂️ Estructura del proyecto
src/
 ├─ main/java/com/jrm/conversor/
 │   ├─ App.java                         # Menú en bucle + Console I/O
 │   └─ service/
 │       ├─ CurrencyApiClient.java       # HttpClient + selección de proveedor + DEBUG
 │       └─ CurrencyConverter.java       # Parseo JSON (Gson) + getRate/convert
 └─ test/java/com/jrm/conversor/
     └─ AppTest.java                     # test del arquetipo (JUnit 4)
pom.xml


🧠 Diseño (resumen)

CurrencyApiClient

Si existe EXCHANGE_API_KEY → ExchangeRate-API /pair/{from}/{to}

Si no existe → Frankfurter /v1/latest?from={from}&to={to}

Maneja timeout, Accept: application/json, y valida Content-Type.

Imprime [DEBUG] Provider=... URL=... (la key se enmascara).

CurrencyConverter

getRate(from,to):

Lee conversion_rate (ExchangeRate-API)

o rates[to] (Frankfurter)

convert(from,to,amount) = amount * rate

App

Menú (while) con 6 opciones + 0 (salir).

Pide amount, valida y muestra resultado formateado.


🌐 Proveedores de tasas

ExchangeRate-API

Requiere EXCHANGE_API_KEY (Plan Free suficiente para pruebas).

Respuesta: { "conversion_rate": 1292.33, ... }

Frankfurter (fallback sin key)

Respuesta: { "base": "USD", "rates": { "ARS": 1292.33 }, ... }

Puedes intercambiar proveedores en CurrencyApiClient si lo deseas.


🧪 Pruebas

Compilar + ejecutar pruebas (JUnit 4 del arquetipo):

mvn clean test


Saltar solo ejecución (compila tests):

mvn -DskipTests clean package


Saltar compilación y ejecución de tests:

mvn -Dmaven.test.skip=true clean package


🚀 Jar ejecutable (fat jar)

Agrega al pom.xml el maven-shade-plugin (si aún no está):

<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-shade-plugin</artifactId>
      <version>3.5.0</version>
      <executions>
        <execution>
          <phase>package</phase>
          <goals><goal>shade</goal></goals>
          <configuration>
            <transformers>
              <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                <mainClass>com.jrm.conversor.App</mainClass>
              </transformer>
            </transformers>
          </configuration>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>


Empaqueta y ejecuta:

mvn -DskipTests clean package
java -jar target/conversor-monedas-1.0-SNAPSHOT-shaded.jar


🧩 Troubleshooting

package com.google.gson does not exist
Falta dependencia o Maven no importó:

Verifica pom.xml con gson:2.10.1

Maven → Reload Project en IntelliJ

Revisa que no estés corriendo otro proyecto padre sin dependencias.

Classpath apunta a ...\Trabajos Curso JAVA\out\production\...
Estás ejecutando otra configuración:

Run → Edit Configurations…

Use classpath of module: conversor-monedas

Ejecuta App desde este módulo.

missing_access_key / HTTP 401/403
La key de ExchangeRate-API no está o es incorrecta.
Soluciones:

Define EXCHANGE_API_KEY correctamente

O usa el fallback (Frankfurter) dejando la variable vacía.

HTTP 404 con /pair/20/10
Ingresaste “20”/“10” en lugar de códigos de moneda (USD, ARS, ...).
El menú ya valida. Reingresa correctamente.

Sin JSON / Content-Type inesperado
Proxy/firewall corporativo podría interceptar.
Agrega proxy a HttpClient si lo necesitas.


🛣️ Roadmap (extras sugeridos)

Historial de conversiones con java.time + exportar CSV/JSON.

Más monedas: COP, BOB, etc.

Formateo local de moneda con NumberFormat/Currency por código destino.

Precisión con BigDecimal para cálculos monetarios.

Tests con JUnit 5 y Mockito.


📸 Capturas / Demo

<img width="1358" height="718" alt="image" src="https://github.com/user-attachments/assets/a1c74d51-120d-4a74-b979-61dee980ce05" />

<img width="720" height="393" alt="image" src="https://github.com/user-attachments/assets/5e679f73-9a4f-4da0-a309-0e8e5b92e09b" />

<img width="747" height="427" alt="image" src="https://github.com/user-attachments/assets/24f23567-b5f8-439c-89df-8c91a6575666" />


📝 Licencia

 Licencia  MIT



👤 Autor

Jose Ricardo Muñoz Mansilla
Proyecto de práctica: Java / APIs / JSON / Maven.
