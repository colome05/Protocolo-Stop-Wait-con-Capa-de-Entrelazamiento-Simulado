# Protocolo TCP con Distribución Cuántica de Claves (BB84) y Capa de Entrelazamiento

Este proyecto extiende la práctica de laboratorio de sockets TCP para implementar una **Simulación de Criptografía Cuántica**. 

El sistema fusiona dos conceptos fundamentales de la física cuántica aplicada a la información: el protocolo **BB84** para la negociación segura de claves y la **Teleportación Cuántica** (Cifrado Vernam) para la transmisión confidencial de datos.

## 📚 Fundamento Teórico

Esta implementación se sustenta en:

1.  **Protocolo BB84 (Bennett & Brassard, 1984):** Permite a dos partes (Alice y Bob) acordar una clave secreta compartida basándose en la aleatoriedad de la medición cuántica. En nuestra simulación, esto asegura que cada conexión TCP tenga una semilla única y segura.
2.  **Teleportación Cuántica / Cifrado Vernam (Boykin & Roychowdhury, 2003):** El mensaje original ($M$) se combina con un recurso compartido ($K$) generado por la semilla negociada, resultando en un texto cifrado ($C$) teóricamente indescifrable sin la clave.

$$C = M \oplus K \quad \xrightarrow{\text{Red}} \quad M = C \oplus K$$

---

## ⚙️ Arquitectura del Sistema

El protocolo se divide en dos fases claramente diferenciadas:

### 1. Fase de Negociación (BB84 Handshake)

Antes de transmitir cualquier dato, el cliente (Alice) y el servidor (Bob) negocian una **semilla generadora** aprovechando el intercambio de paquetes `SYN` del protocolo TCP.

* **Alice (Cliente - `connect`):**
    1.  Genera una cadena de bits aleatorios (simulando qubits).
    2.  Genera una cadena de bases aleatorias (simulando filtros de polarización).
    3.  Envía **ambos** (Bits + Bases) en el payload del primer `SYN`.

* **Bob (Servidor - `accept`):**
    1.  Recibe el `SYN`.
    2.  Genera sus propias bases aleatorias.
    3.  **Cribado:** Compara sus bases con las de Alice. Solo conserva los bits donde las bases coinciden.
    4.  Calcula la semilla resultante (`negotiatedSeed`) y la inyecta en el socket.
    5.  Responde en el `SYN-ACK` enviando **solo sus bases** (sin los bits).

* **Alice (`processReceivedSegment`):**
    1.  Recibe las bases de Bob.
    2.  Realiza el mismo proceso de cribado sobre sus bits originales.
    3.  Obtiene la misma `negotiatedSeed` sin que la clave completa haya viajado nunca por la red.

### 2. Fase de Transmisión (Entrelazamiento Simulado)

Una vez establecida la `negotiatedSeed`, se inicializan los generadores aleatorios (`Random`) en ambos extremos. Esto simula tener una fuente continua de pares de fotones entrelazados.

| Variable | En el Código | Analogía Cuántica |
| :--- | :--- | :--- |
| **$K$ (Clave)** | Byte generado por `Random(negotiatedSeed)`. | **Par de fotones entrelazados.** Recurso correlacionado generado gracias a BB84. |
| **$M$ (Mensaje)** | El dato útil a transmitir. | El estado cuántico del qubit original. |
| **Emisor** | Realiza `C = M ^ K`. | **Medición de Bell.** Alice interactúa su qubit con su mitad del par entrelazado. |
| **Canal** | Protocolo TCP transportando bytes cifrados. | **Canal Clásico.** Envío de los resultados de la medición (bits clásicos). |
| **Receptor** | Realiza `M = C ^ K` para recuperar el dato. | **Correcciones de Pauli.** Bob usa su mitad del par para reconstruir el estado original. |

---

## 🔐 Mejoras de Seguridad Implementadas

A diferencia de implementaciones con semillas estáticas, esta versión ofrece:

* **Semillas Dinámicas por Sesión:** Cada conexión (cliente/puerto) genera una clave distinta mediante el proceso BB84. Múltiples clientes pueden conectarse simultáneamente y cada uno tendrá su propio canal seguro independiente.
* **One-Time Pad (Libreta de un solo uso):** La clave de cifrado ($K$) tiene la misma longitud que el mensaje y nunca se reutiliza, garantizando secreto perfecto dentro de la simulación.
* **Simulación de Qubits:** Se utilizan cadenas de caracteres y operaciones lógicas para emular el comportamiento de filtrado de bases, demostrando cómo se puede establecer un secreto compartido sobre un canal público.

## 🚀 Ejecución

La simulación es autocontenida y se ejecuta directamente desde el entorno de desarrollo.

1.  Abrir el proyecto en **NetBeans** (o IDE compatible) en el ordenador.
2.  Navegar al paquete correspondiente según la fase que se quiera probar:
    * **Negociación BB84:** `practica7.conCapaDeEntrelazamiento`
    * **Transmisión de Datos:** `practica5.conCapaDeEntrelazamiento`
3.  Localizar el archivo **`Test.java`** dentro del paquete.
4.  Hacer clic derecho sobre el archivo y seleccionar **"Run File"**.
5.  Verificar en la consola la negociación de la semilla y la posterior transmisión.
