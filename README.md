# Protocolo Stop & Wait con Capa de Entrelazamiento Simulado

He adaptado el protocolo **Stop & Wait** de una práctica de laboratorio (Práctica 5) para simular **Teleportación Cuántica**. 

## 📚 Fundamento Teórico

Esta implementación se basa en la equivalencia formal entre la **Teleportación Cuántica** y el **Cifrado Vernam Cuántico** (*Quantum One-Time Pad*), descrita por **Boykin y Roychowdhury (2003)**. 

La premisa es que el mensaje original ($M$) se combina con un recurso compartido aleatorio ($K$, que representa el entrelazamiento), resultando en un texto cifrado ($C$) estadísticamente indistinguible del ruido para quien no posea $K$.

$$C = M \oplus K \quad \xrightarrow{\text{Red}} \quad M = C \oplus K$$ 

## ⚙️ Funcionamiento y Analogías

La simulación utiliza un **generador aleatorio con semilla compartida** para emular el par de fotones entrelazados. Al ser determinista para quien tiene la semilla, pero aleatorio para quien no, logramos el efecto de "correlación a distancia".

| Variable | En el Código | Analogía Cuántica |
| :--- | :--- | :--- |
| **$K$ (Clave)** | Byte generado por la semilla fija compartida. | **Par de fotones entrelazados.** Recurso preexistente entre Alice y Bob. |
| **$M$ (Mensaje)** | El dato útil a transmitir. | El estado cuántico del qubit original. |
| **Emisor** | Realiza `C = M ^ K`. | **Medición de Bell.** Alice interactúa su qubit con su mitad del par entrelazado. |
| **Canal** | Protocolo Stop & Wait transportando bytes "random". | **Canal Clásico.** Envío de los resultados de la medición (bits clásicos). |
| **Receptor** | Realiza `M = C ^ K` para recuperar el dato. | **Correcciones de Pauli.** Bob usa su mitad del par para reconstruir el estado original. |

> **Resultado:** Gracias a la reversibilidad del XOR, logramos el **100% de integridad** en la práctica, cumpliendo con la simulación física: el dato viaja protegido por correlación y no por encriptación tradicional de clave pública.

## 🔐 Nota sobre Seguridad y Criptografía

Conceptualmente, esto es una variante del **Cifrado Vernam**. Sin embargo, la diferencia clave con un Vernam clásico reside en el origen de la clave ($K$):
* En esta simulación, la clave no se envía; **se "manifiesta" simultáneamente** en ambos extremos gracias a la semilla compartida, emulando la **no-localidad** cuántica.
* **Futuras Mejoras:** Actualmente la semilla es estática. Una implementación más realista incluiría un *handshake* previo simulando el protocolo **BB84** (distribución cuántica de claves) para negociar esta semilla de forma segura sin posibilidad de intercepción.
