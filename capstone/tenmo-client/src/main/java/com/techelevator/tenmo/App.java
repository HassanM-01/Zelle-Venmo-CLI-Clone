package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountServices;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.Arrays;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);


    private AuthenticatedUser currentUser;


    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                Transfer[] transfers = getTransferHistory();
                consoleService.transferHistory(currentUser, transfers);
                Long id = getTransferIDFromUser();
                Transfer transfer = verifyTransferExists(id, transfers);
                consoleService.printTransfer(transfer);
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        AccountServices accountServices = new AccountServices(API_BASE_URL, currentUser);
        BigDecimal balance;
        try {
            balance = accountServices.returnBalance();
            System.out.println(balance);
        } catch (RestClientException e) {
            System.out.println("No balance, you're broke :(");
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    private Transfer[] getTransferHistory() {
        TransferService transferService = new TransferService(API_BASE_URL, currentUser);
        return transferService.getTransfers();
    }

    public Long getTransferIDFromUser() {
        Long transferId = consoleService.promptForLong("Please enter transfer ID to view details (0 to cancel): ");
        if (transferId == 0) {
            mainMenu();
        } else {
            transferId += 3000;
        }
        return transferId;
    }

    public Long returnsUserIdLong (Long userId){
        if (userId == 0) {
            mainMenu();
        }
        if (userId < 0) {
            consoleService.printErrorMessage();
        }
        userId += 1000;

        return userId;
    }

    public Transfer verifyTransferExists(Long id, Transfer[] transfers) {
       Transfer newTransfer = null;
        for (Transfer transfer : transfers) {
            if(transfer.getTransferId().equals(id)) {
                newTransfer = transfer;
            }
        }
        return newTransfer;
    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub
    }

    private void sendBucks() {
        AccountServices accountServices = new AccountServices(API_BASE_URL, currentUser);

        User[] users = accountServices.getUsers();
        consoleService.transferFundsPrompt(users);

        Long userId = consoleService.promptForLong("Enter ID of user you are sending to (0 to cancel):");

        Long parsedUserId = returnsUserIdLong(userId);
        parsedUserId = verifyAccountExists(parsedUserId, users);
        if (parsedUserId == 0){
            mainMenu();
        }
        BigDecimal transferAmount = consoleService.promptForBigDecimal("Enter amount: ");

        Long transferType = Long.valueOf(1);

        consoleService.printString(sendTransfer(parsedUserId,transferAmount, transferType));

    }


    public String sendTransfer(Long parsedUserId, BigDecimal transferAmount, Long transferType){
        TransferService transferService = new TransferService(API_BASE_URL, currentUser);

        Transfer transfer = new Transfer(parsedUserId, transferAmount, transferType);
        if (transferService.sendFunds(transfer)) {
            return "Transaction Successful!";
        } else {
            return "Transaction Failed!";
        }

    }



    public Long verifyAccountExists(Long id, User[] users) {
        for (User user : users) {
            if(user.getId().equals(id)) {
                return id;
            }
        }
        System.out.println("\nThis id is not associated with an account, please try again.\n");
        return Long.valueOf(0);
    }

    private void requestBucks() {
        // TODO Auto-generated method stub

    }

}
