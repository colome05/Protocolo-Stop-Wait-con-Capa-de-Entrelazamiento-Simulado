# Protocolo-Stop-Wait-con-Capa-de-Entrelazamiento-Simulado
He adaptado el protocolo Stop &amp; Wait de una práctica de laboratorio para simular Teleportación Cuántica. Para el entrelazamiento, uso un generador aleatorio con semilla compartida. Los datos se cifran con XOR antes de enviarse, simulando la medición física: por la red solo viaja "ruido" y solo el receptor, con la misma semilla, puede reconstruir el mensaje original.

Funcionamiento del Protocolo
1. El Emisor (Simulando la Medición de Bell) En lugar de simplemente comprimir o enviar el dato tal cual, lo que hace el código es combinar el dato real con un byte semi-aleatorio (generado a partir de una semilla fija). La operación es XOR (data ^ random_byte).
¿Qué conseguimos? Convertir la información en algo totalmente ilegible si se mira desde fuera.
Analogía cuántica: Esto simula el momento en que Alice realiza la "Bell State Measurement", interactuando su qubit de datos con su mitad del par de fotones entrelazados. El resultado clásico que obtenemos depende de esa correlación.
2. La Red (El Canal Clásico) Para mover esa información, utilizamos el protocolo Stop & Wait. Lo que viaja por el cable es ese byte "transformado".
Seguridad: Si ponemos un ‘sniffer’ en el canal, lo único que veremos son números aleatorios sin sentido. El dato original viaja oculto tras esa aleatoriedad, simulando que sin la otra parte del par entrelazado, la información es indescifrable.
3. El Receptor (Reconstrucción y Corrección) Al recibir el byte, el receptor entra en juego. Como comparte la misma semilla inicial que el emisor (lo que representa tener la otra mitad del par entrelazado), genera exactamente la misma secuencia de números aleatorios.
La operación: Simplemente aplica la inversa (XOR de nuevo).
Analogía cuántica: Esto representa a Bob usando su par entrelazado y aplicando las correcciones de Pauli necesarias para recuperar el estado cuántico original intacto.
Resultado: Gracias a que la operación XOR es matemáticamente reversible, la recuperación es exacta, bit a bit. Si envías un 255, al otro lado aparece un 255. El test de la práctica da el visto bueno porque logramos el 100% de integridad: hemos cumplido con la simulación física (protegiendo el dato por correlación) y con el requisito de la práctica (el dato llega perfecto).
