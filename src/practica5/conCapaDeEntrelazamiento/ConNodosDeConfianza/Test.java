package practica5.conCapaDeEntrelazamiento.ConNodosDeConfianza;

import util.SimNet_FullDuplex;
import practica4.Protocol;
import util.Const;
import util.Receiver;
import util.Sender;
import util.SimNet;

public class Test {

    public static void main(String[] args) {

        // ENLACE 1: Alice <---> Trusted Node
        SimNet_FullDuplex linkAliceToNode = new SimNet_FullDuplex(0, 0);

        // ENLACE 2: Trusted Node <---> Bob
        SimNet_FullDuplex linkNodeToBob = new SimNet_FullDuplex(0, 0);

        // 1. ALICE (HostSnd)
        // Se conecta al extremo 'Snd' del enlace 1.
        // IMPORTANTE: Ahora Alice envía al puerto del Trusted Node (50), no al de Bob (80).
        // Tendremos que modificar HostSnd ligeramente para apuntar al puerto 50.
        new Thread(new HostSnd(linkAliceToNode.getSndEnd(), TrustedNode.IN_PORT)).start();

        // 2. TRUSTED NODE
        // Tiene acceso al extremo 'Rcv' del enlace 1 (para escuchar a Alice)
        // Y al extremo 'Snd' del enlace 2 (para hablar con Bob)
        new Thread(new TrustedNode(linkAliceToNode.getRcvEnd(), linkNodeToBob.getSndEnd(), HostRcv.PORT)).start();

        // 3. BOB (HostRcv)
        // Se conecta al extremo 'Rcv' del enlace 2.
        new Thread(new HostRcv(linkNodeToBob.getRcvEnd())).start();
    }
}

class HostSnd implements Runnable {

    public static final int PORT = 10;
    protected Protocol proto;
    protected int destPort; // Nuevo campo para flexibilidad

    // Constructor modificado para aceptar puerto de destino
    public HostSnd(SimNet net, int destPort) {
        this.proto = new Protocol(net);
        this.destPort = destPort;
    }

    // Constructor antiguo (por compatibilidad si hace falta)
    public HostSnd(SimNet net) {
        this(net, HostRcv.PORT);
    }

    public void run() {
        // Ahora envía al destPort (que será el puerto 50 del Nodo)
        new Sender(new TSocket(proto, HostSnd.PORT, destPort)).start();
    }
}

// HostRcv no cambia, simplemente escucha en su puerto 80 como siempre.
class HostRcv implements Runnable {

    public static final int PORT = 80;
    protected Protocol proto;

    public HostRcv(SimNet net) {
        this.proto = new Protocol(net);
    }

    public void run() {
        // Bob escucha en el 80. El Trusted Node le enviará datos aquí.
        new Receiver(new TSocket(proto, HostRcv.PORT, TrustedNode.OUT_PORT)).start();
    }
}
