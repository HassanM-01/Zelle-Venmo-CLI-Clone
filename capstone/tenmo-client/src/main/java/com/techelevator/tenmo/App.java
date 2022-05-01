package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountServices;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

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
                viewTransfers((long) 2);
            } else if (menuSelection == 3) {
                viewTransfers((long) 1);
            } else if (menuSelection == 4) {
                sendOrRequestBucks((long) 2);
            } else if (menuSelection == 5) {
                sendOrRequestBucks((long) 1);
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    public void viewTransfers(Long transferType) {
        Transfer[] transfers = getTransferHistory();
        if (transferType == 2){
            consoleService.transferHistory(currentUser, transfers, "Transfers");
            Long id = getTransferIDFromUser();
            Transfer transfer = verifyTransferExists(id, transfers);
            consoleService.printTransfer(transfer);
        } else {
            transfers = filterOutNonPendingTransfers (transfers);
            consoleService.transferHistory(currentUser, transfers, "Pending Transfers");
            Long id = getTransferIDFromUser();
            Transfer transfer = verifyTransferExists(id, transfers);
            consoleService.printTransfer(transfer);
            acceptOrDeclinePendingRequests(transfer);
        }
    }

    private Transfer[] filterOutNonPendingTransfers(Transfer[] transfers) {
        return  Arrays.stream(transfers).filter(x->x.getTransferType().equals((long)1)
                && !x.getToUsername().equals(currentUser.toString())
                && (x.getTransferStatus().equals((long)1))).toArray(Transfer[]:: new);
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

    public Transfer verifyTransferExists(Long id, Transfer[] transfers) {
        Transfer newTransfer = null;
        for (Transfer transfer : transfers) {
            if(transfer.getTransferId().equals(id)) {
                newTransfer = transfer;
            }
        }
        return newTransfer;
    }

    private void viewCurrentBalance() {
        AccountServices accountServices = new AccountServices(API_BASE_URL, currentUser);
        BigDecimal balance;
        try {
            balance = accountServices.returnBalance();
            System.out.println(balance);
        } catch (Exception e) {
            consoleService.printString("Failed to retrieve balance, please try again.");
        }
    }

    public String sendTransfer(Long parsedUserId, BigDecimal transferAmount, Long transferType){
        TransferService transferService = new TransferService(API_BASE_URL, currentUser);
        Transfer transfer = new Transfer(parsedUserId, transferAmount, transferType);
          if (transfer.getTransferType() == 2) {
              if (transferService.sendFunds(transfer)) {
                  return "Transaction Successful!";
              } else {
                  return "Transaction Failed!";
              }
          } else {
              if (transferService.sendTranserRequest(transfer)) {
                  return "Request was sent!";
              } else {
                  return "Request was not sent :(";
              }
          }
    }

    private void sendOrRequestBucks(Long transferType) {
        User[] listOfUsers = getUsers();
        consoleService.transferFundsPrompt(listOfUsers);
        Long destinationAccountsUserId;
        if (transferType == 2){
            destinationAccountsUserId = consoleService.promptForLong("Enter ID of user you are sending to (0 to cancel): ");
        } else {
            destinationAccountsUserId = consoleService.promptForLong("Enter ID of user you are requesting from(0 to cancel): ");
        }
        Long verifiedUserId = verifyUserIdCorrespondsToAnAccount(destinationAccountsUserId, listOfUsers);
        if (verifiedUserId == 0) {
            mainMenu();
        } else {
            BigDecimal transferAmount = getPositiveTransferAmount();
            consoleService.printString(sendTransfer(verifiedUserId, transferAmount, transferType));
        }
    }

    private User[] getUsers() {
        AccountServices accountServices = new AccountServices(API_BASE_URL, currentUser);
        return accountServices.getUsers();
    }

    public Long verifyUserIdCorrespondsToAnAccount(Long id, User[] users) {
        if (id == 0) {
            mainMenu();
        } else {
            id += 1000;
            for (User user : users) {
                if(user.getId().equals(id)) {
                    return id;
                }
            }
        }
        consoleService.printString("\nThis id is not associated with an account, please try again.\n");
        return (long) 0;
    }

    public BigDecimal getPositiveTransferAmount() {
        BigDecimal transferAmount = consoleService.promptForBigDecimal("Enter amount: ");
        while (transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
            transferAmount = consoleService.promptForBigDecimal("Please enter an amount greater than zero: ");
        }
        return transferAmount;
    }

    private void acceptOrDeclinePendingRequests(Transfer transfer) {
        TransferService transferService = new TransferService(API_BASE_URL, currentUser);
        consoleService.printPromptPendingRequests();
        int choice = consoleService.promptForInt("");
        switch (choice){
            case 1: if (transferService.approveTransfer(transfer)){
                System.out.println("This transaction has been approved!");
            } else {
                System.out.println("This transaction could not be approved");
            }
                break;
            case 2 : transferService.declineTransfer(transfer);
                System.out.println("This transaction was successfully declined");
                break;
            case 3 : mainMenu();
                break;
            default: mainMenu();
        }
    }
}

