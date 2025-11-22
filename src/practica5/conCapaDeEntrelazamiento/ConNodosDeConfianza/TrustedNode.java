package practica5.conCapaDeEntrelazamiento.ConNodosDeConfianza;

import util.SimNet;
import util.TSocket_base;
import practica4.Protocol;


// ESTE CODIGO HA SIDO GENERADO POR IA, NO TENGO LOS CONOCIMIENTOS SUFICIENTES
// COMO PARA SIMULAR UNA INTERFAZ RUNNABLE MULTITHREADING


// El Trusted Node actúa como un repetidor regenerativo
public class TrustedNode implements Runnable {

    public static final int IN_PORT = 50;  // Puerto donde escucha a Alice
    public static final int OUT_PORT = 60; // Puerto desde donde envía a Bob
    
    // Necesitamos dos interfaces de red: una hacia Alice, otra hacia Bob
    protected Protocol protoIn;
    protected Protocol protoOut;
    protected int targetPort; // El puerto de Bob

    public TrustedNode(SimNet netFromAlice, SimNet netToBob, int targetPort) {
        this.protoIn = new Protocol(netFromAlice);
        this.protoOut = new Protocol(netToBob);
        this.targetPort = targetPort;
    }

    @Override
    public void run() {
        // 1. Socket de entrada (Lado Alice): Actúa como receptor
        TSocket socketReceiver = new TSocket(protoIn, IN_PORT, HostSnd.PORT);
        
        // 2. Socket de salida (Lado Bob): Actúa como emisor
        TSocket socketSender = new TSocket(protoOut, OUT_PORT, HostRcv.PORT);

        System.out.println("=== TRUSTED NODE INICIADO Y SEGURO ===");
        
        // Buffer interno (Memoria Clásica del Nodo)
        byte[] classicalBuffer = new byte[1000]; 

        while (true) {
            // --- FASE 1: MEDICIÓN (Quantum Measurement) ---
            // Los fotones llegan de Alice. El nodo los mide.
            // Al hacer 'receiveData', el estado cuántico colapsa a bits clásicos.
            int nBytes = socketReceiver.receiveData(classicalBuffer, 0, classicalBuffer.length);

            if (nBytes > 0) {
                System.out.println("\n[Trusted Node] 1. Fotones interceptados y medidos.");
                System.out.println("[Trusted Node] 2. Estado almacenado como información clásica (" + nBytes + " bytes).");

                // Aquí los datos residen en 'classicalBuffer' como bits normales.
                // Si un hacker entra AQUÍ físicamente, roba la clave.
                // Por eso el nodo debe ser "Trusted" y estar protegido.

                // --- FASE 2: RE-ENCRIPTACIÓN (Quantum Re-generation) ---
                // El nodo usa la información clásica para polarizar nuevos fotones
                // y enviárselos a Bob.
                System.out.println("[Trusted Node] 3. Generando nuevos fotones polarizados hacia Bob...");
                socketSender.sendData(classicalBuffer, 0, nBytes);
            }
        }
    }
}