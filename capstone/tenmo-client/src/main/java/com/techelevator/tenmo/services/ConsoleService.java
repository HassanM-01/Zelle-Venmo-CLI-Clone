package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public Long promptForLong(String prompt) {
        System.out.println(prompt);
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void transferFundsPrompt(User[] users) {
        System.out.println("-------------------------------------------");
        System.out.println("Users");
        System.out.println("ID          Name");
        System.out.println("-------------------------------------------");
        for (User user : users) {
            System.out.println(user.getId().toString().substring(1) + "         " + user.getUsername());
        }
        System.out.println("---------");
    }

    public void printFullTransferHistory(AuthenticatedUser authenticatedUser, Transfer[] transfers) {
        if (transfers.length == 0) {
            System.out.println("\n*No pending transfers!*\n");
        } else {
            System.out.println("-------------------------------------------");
            System.out.println("Transfers");
            System.out.println("ID              Name                 Amount");
            System.out.println("-------------------------------------------");
            for (Transfer transfer : transfers) {
                // if the current user's name is the same name as the destination account's
                // user name then the current user received those funds
                if (transfer.getToUsername().equals(authenticatedUser.getUser().getUsername())) {
                    System.out.println(transfer.getTransferId().toString().substring(1) +
                            "          From: " + transfer.getFromUsername() +
                            "              $" + transfer.getTransferAmount());
                } else {
                    System.out.println(transfer.getTransferId().toString().substring(1) +
                            "            To: " + transfer.getToUsername() +
                            "              $" + transfer.getTransferAmount());
                }
            }
            System.out.println("---------");
        }
    }

    public void printPendingTransfers(Transfer[] transfers) {
        if (transfers.length == 0) {
            System.out.println("\n*No pending transfers!*\n");
        } else {
            System.out.println("-------------------------------------------");
            System.out.println("Pending Transfers");
            System.out.println("ID            To                   Amount");
            System.out.println("-------------------------------------------");
            for (Transfer transfer : transfers) {
                System.out.println(transfer.getTransferId().toString().substring(1) +
                        "          " + transfer.getToUsername() + "                $"
                        + transfer.getTransferAmount());
            }
            System.out.println("---------");
        }
    }

    public void printString(String word){
        System.out.println(word);
    }

    public void printTransfer(Transfer transfer) {
        if (transfer == null) {
            System.out.println("This is not a valid transfer id, please select again.");
        } else {
            System.out.println("--------------------------------------------");
            System.out.println("Transfer Details");
            System.out.println("--------------------------------------------");
            System.out.println("Id: " + transfer.getTransferId().toString().substring(2));
            System.out.println("From: " + transfer.getFromUsername());
            System.out.println("To: " + transfer.getToUsername());
            System.out.println("Type: " + convertTransferTypeToWord(transfer.getTransferType()));
            System.out.println("Status: " + convertTransferStatusToWord(transfer.getTransferStatus()));
            System.out.println("Amount: $" + transfer.getTransferAmount());

        }
    }

    public void printPromptPendingRequests() {
        System.out.println("---------");
        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("3: Don't approve or reject");
        System.out.println("---------");
        System.out.println("Please choose an option: ");
    }

    public String convertTransferStatusToWord(Long id) {
        if (id == 1) {
            return "Pending";
        } else if (id == 2) {
            return "Approved";
        } else {
            return "Rejected";
        }
    }

    public String convertTransferTypeToWord(Long id) {
        if (id == 1) {
            return "Request";
        } else {
            return "Send";
        }
    }
}
