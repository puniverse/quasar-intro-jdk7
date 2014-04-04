package foo;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.Strand;
import co.paralleluniverse.strands.SuspendableRunnable;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;

public class Main {
    public static void main(String[] args) throws Exception {
        final Channel<Integer> c = Channels.newChannel(0);

        new Fiber<Void>(new SuspendableRunnable() {

            @Override
            public void run() throws SuspendExecution, InterruptedException {
                for (int i = 0; i < 10; i++) {
                    c.send(i);
                    Strand.sleep(500);
                }
                c.close();
            }
        }).start();
        
        Integer x;
        while((x = c.receive()) != null)
            System.out.println("=> " + x);
    }
}
