package shinil.direct.share.thread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import shinil.direct.share.util.Constants;

/**
 * @author shinilms
 */

public final class StartProxyThread extends Thread {

    private boolean isTrue = true;
    private ServerSocket serverSocket;

    public boolean isSocketBound() {
        return serverSocket != null && serverSocket.isBound();
    }

    public StartProxyThread() {
        try {
            this.serverSocket = new ServerSocket();
            this.serverSocket.setReuseAddress(true);
            this.serverSocket.bind(new InetSocketAddress(Constants.PROXY_PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        while (isTrue) {
            try {
                new ProxyConnectionThread(serverSocket.accept());
            } catch (Exception e) {/*ignore*/}
        }
    }

    public void stopProxy() {
        isTrue = false;
        try {
            serverSocket.close();
            serverSocket = null;
        } catch (Exception e) {/*ignore*/}
    }
}
