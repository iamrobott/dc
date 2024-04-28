import java.io.*;

class BullyAlgo {
    int cood, ch, crash;
    int prc[];

    public void election(int n) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(" The Coordinator Has Crashed!");
        int flag = 1;
        while (flag == 1) {
            crash = 0;
            for (int il = 0; il < n; il++)
                if (prc[il] == 0)
                    crash++;
            if (crash == n) {
                System.out.println(" **** All Processes Are Crashed ****");
                break;
            } else {
                System.out.println("\n Enter the Initiator");
                int init = Integer.parseInt(br.readLine());
                if ((init < 1) || (init > n) || (prc[init - 1] == 0)) {
                    System.out.println(" Invalid Initiator");
                    continue;
                }
                for (int il = init - 1; il < n; il++) {
                    System.out.println(" Process " + (il + 1) + " Called for Election");
                    System.out.println("");
                    for (int il1 = init - 1; il1 < n; il1++) {
                        if (prc[il1] == 0)
                            System.out.println(" Process " + (il1 + 1) + " Is Dead");
                        else
                            System.out.println(" Process " + (il1 + 1) + " Is In");
                    }
                    for (int il2 = n - 1; il2 >= 0; il2--)
                        if (prc[il2] == 1) {
                            cood = (il2 + 1);
                            System.out.println(" *** New Coordinator Is " + (cood) + " ***");
                            flag = 0;
                            break;
                        }
                }
            }
        }
    }

    public void Bully() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(" Enter The Number of Processes");
        int n = Integer.parseInt(br.readLine());
        prc = new int[n];
        crash = 0;
        for (int i = 0; i < n; i++)
            prc[i] = 1;
        cood = n;
        do {
            System.out.println(" \t1. Crash A Process");
            System.out.println(" \t2. Recover A Process");
            System.out.println(" \t3. Display New Coordinator");
            System.out.println(" \t4. Exit");
            ch = Integer.parseInt(br.readLine());
            switch (ch) {
                case 1:
                    System.out.println(" Enter A Process To Crash");
                    int cp = Integer.parseInt(br.readLine());
                    if ((cp > n) || (cp < 1))
                        System.out.println(" Invalid Process! Enter A Valid Process");
                    else if ((prc[cp - 1] == 1) & (cood != cp)) {
                        prc[cp - 1] = 0;
                        System.out.println(" Process " + cp + " Has Been Crashed");
                    } else if ((prc[cp - 1] == 1) & (cood == cp)) {
                        prc[cp - 1] = 0;
                        election(n);
                    } else
                        System.out.println(" Process " + cp + " Is Already Crashed");
                    break;
                case 2:
                    System.out.println(" Crashed Processes Are: ");
                    for (int i = 0; i < n; i++) {
                        if (prc[i] == 0) {
                            System.out.println(i + 1);
                            crash++;
                        }
                    }
                    System.out.println(" Enter The Process You Want To Recover: ");
                    int rp = Integer.parseInt(br.readLine());
                    if ((rp < 1) || (rp > n))
                        System.out.println(" Invalid Process. Enter A Valid ID");
                    else if ((prc[rp - 1] == 0) & (rp > cood)) {
                        prc[rp - 1] = 1;
                        System.out.println(" Process " + rp + " Has Recovered");
                        cood = rp;
                        System.out.println(" Process " + rp + " Is The New Coordinator");
                    } else if (crash == n) {
                        prc[rp - 1] = 1;
                        cood = rp;
                        System.out.println(" Process " + rp + " Is The New Coordinator");
                        crash--;
                    } else if ((prc[rp - 1] == 0) & (rp < cood)) {
                        prc[rp - 1] = 1;
                        System.out.println(" Process " + rp + " Has Recovered");
                    } else
                        System.out.println(" Process " + rp + " Has Not A Crashed process");
                    break;
                case 3:
                    System.out.println(" Current Coordinator Is " + cood);
                    break;
                case 4:
                    return; // Return to main menu
                default:
                    System.out.println(" Invalid Entry!");
                    break;
            }
        } while (ch != 4);
    }
}

class RingElection {
    class Process {
        int id;
        boolean active;

        Process(int id) {
            this.id = id;
            active = true;
        }
    }

    int totalProcesses;
    Process[] processes;

    public void initializeSystem(int n) {
        System.out.println("No of processes: " + n);
        totalProcesses = n;
        processes = new Process[totalProcesses];
        for (int i = 0; i < totalProcesses; i++) {
            processes[i] = new Process(i);
        }
    }

    public void initiateElection(int initiatorId) {
        System.out.println("Election Initiated by " + initiatorId);

        int initializedProcess = initiatorId;

        int old = initializedProcess;
        int newer = (old + 1) % totalProcesses;

        while (true) {
            if (processes[newer].active) {
                System.out.println("Process " + processes[old].id + " passes Election(" + processes[old].id + ") to Process " + processes[newer].id);
                old = newer;
            }

            newer = (newer + 1) % totalProcesses;
            if (newer == initializedProcess) {
                break;
            }
        }

        System.out.println("Process " + processes[findMaximum()].id + " becomes coordinator");
        int coordinator = processes[findMaximum()].id;

        old = coordinator;
        newer = (old + 1) % totalProcesses;

        while (true) {
            if (processes[newer].active) {
                System.out.println("Process " + processes[old].id + " passes Coordinator(" + coordinator + ") message to Process " + processes[newer].id);
                old = newer;
            }
            newer = (newer + 1) % totalProcesses;
            if (newer == coordinator) {
                System.out.println("End Of Election");
                break;
            }
        }
    }

    public int findMaximum() {
        int index = 0;
        int maxId = -9999;
        for (int i = 0; i < processes.length; i++) {
            if (processes[i].active && processes[i].id > maxId) {
                maxId = processes[i].id;
                index = i;
            }
        }
        return index;
    }

    public void recoverCoordinator(int processId) {
        if (processId >= 0 && processId < totalProcesses) {
            processes[processId].active = true;
            System.out.println("Process " + processId + " has recovered. It's now active.");
        } else {
            System.out.println("Invalid process ID.");
        }
    }

    public void crashProcess(int processId) {
        if (processId >= 0 && processId < totalProcesses) {
            processes[processId].active = false;
            System.out.println("Process " + processId + " has been marked as crashed!");
        } else {
            System.out.println("Invalid process ID.");
        }
    }
}

class ElectionAlgorithms {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BullyAlgo bully = new BullyAlgo();
        RingElection ring = new RingElection();

        int choice = 0;
        while (choice != 3) {
            System.out.println("\nMenu:");
            System.out.println("1. Bully Election Algorithm");
            System.out.println("2. Ring Election Algorithm");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(reader.readLine());

            switch (choice) {
                case 1:
                    bully.Bully();
                    break;
                case 2:
                    System.out.print("Enter the number of processes: ");
                    int numProcesses = Integer.parseInt(reader.readLine());
                    ring.initializeSystem(numProcesses);

                    int ringChoice = 0;
                    while (ringChoice != 4) {
                        System.out.println("\nRing Election Menu:");
                        System.out.println("1. Initiate Election");
                        System.out.println("2. Crash Process");
                        System.out.println("3. Recover Process");
                        System.out.println("4. Exit");
                        System.out.print("Enter your choice: ");
                        ringChoice = Integer.parseInt(reader.readLine());

                        switch (ringChoice) {
                            case 1:
                                System.out.print("Enter the process ID to initiate the election: ");
                                int initiatorId = Integer.parseInt(reader.readLine());
                                ring.initiateElection(initiatorId);
                                break;
                            case 2:
                                System.out.print("Enter the process ID to crash: ");
                                int crashId = Integer.parseInt(reader.readLine());
                                ring.crashProcess(crashId);
                                break;
                            case 3:
                                System.out.print("Enter the process ID to recover: ");
                                int recoverId = Integer.parseInt(reader.readLine());
                                ring.recoverCoordinator(recoverId);
                                break;
                            case 4:
                                break;
                            default:
                                System.out.println("Invalid choice! Please enter again.");
                        }
                    }
                    break;
                case 3:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice! Please enter again.");
            }
        }

        reader.close();
    }
}
