import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class BlackJack {

    ArrayList<Card> deck;
    Random random = new Random();

    // dealer
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerSum ;
    int dealerAceCount;

    // player
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;

    // gaim Window
    int boardWidth = 600;
    int boardHeight = 600;

    int cardwidth = 110;
    int cardHeight = 154;

    JFrame frame = new JFrame("Black Jack");
    JPanel gamePaenl = new JPanel(){
        @Override
        public void paintComponent(Graphics g){

            super.paintComponent(g);
            try {
                // draw hidden card
                Image hiddenCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                if(!stayButton.isEnabled()){
                    hiddenCardImg = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
                }
                g.drawImage(hiddenCardImg, 20,20, cardwidth, cardHeight, null);


                // 딜러의 카드덱을 그린다.
                for(int i =0; i<dealerHand.size(); i++){
                    Card  card = dealerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, cardwidth +  25 + (cardwidth +5) *i , 20, cardwidth, cardHeight, null);
                }

                // 플레이어 카드덱을 그린다.
                for(int i=0; i<playerHand.size(); i++){
                    Card card = playerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, 20 +(cardwidth + 5)* i, 320,cardwidth,cardHeight,null);
                }

                // STAY버튼을 눌렀을 경우 게임 승패를 계산한다.
                if(!stayButton.isEnabled()){
                    dealerSum = reduceDealerAce();
                    playerSum = reducePlayerAce();
                    System.out.println("STAY : ");
                    System.out.println(dealerSum);
                    System.out.println(playerSum);

                    String message = "";
                    if(playerSum > 21){
                        message = "You Lose";
                    }else if(dealerSum > 21){
                        message = "You Win";
                    }else if (playerSum == dealerSum){
                        message = "Tie";
                    }else if (playerSum < dealerSum){
                        message = "You Win!";
                    }else if (playerSum < dealerSum){
                        message = "You Lose!";
                    }

                    hitbutton.setFont(new Font("Arial", Font.PLAIN, 30));
                    g.setColor(Color.white);
                    g.drawString(message, 220, 250);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    };
    JPanel buttonPanel = new JPanel();
    JButton hitbutton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");


    public void startGame(){
        //deck
        buildDeck();
        shuffleDeck();

        // 딜러의 카드 변수 초기화
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size()-1) ; // 마지막 카드를 remove;
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;

        Card card = deck.remove(deck.size() -1);
        dealerHand.add(card);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce()? 1:0;


        System.out.println("DEALER:");
        System.out.println(hiddenCard);
        System.out.println(dealerHand);
        System.out.println(dealerSum);
        System.out.println(dealerAceCount);

        // 플레이어의 카드 변수 초기화
        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceCount = 0;

        for(int i =0; i<2; i++){
            card = deck.remove(deck.size() -1);
            playerSum += card.getValue();
            playerAceCount += card.isAce()? 1: 0;
            playerHand.add(card);
        }

        System.out.println("PLAYER: ");
        System.out.println(playerHand);
        System.out.println(playerSum);
        System.out.println(playerAceCount);
    }



    BlackJack(){
        startGame();

        // 게임 윈도우 셋팅
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePaenl.setLayout(new BorderLayout());
        gamePaenl.setBackground(new Color(53,101,77));
        frame.add(gamePaenl);

        // 게임 윈도우에 버튼 구현하기
        hitbutton.setFocusable(false);
        buttonPanel.add(hitbutton);
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // HIT 버튼 기능
        hitbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Card card = deck.remove(deck.size()-1);
                playerSum += card.getValue();
                playerAceCount += card.isAce() ? 1 : 0;
                playerHand.add(card);

                // ACE카드를 뽑은 경우 현재 카드 합에 따라 계산
                if(reducePlayerAce()> 21){ // A + 2 + J  -->  1 : 2 + J
                    hitbutton.setEnabled(false);
                }

                gamePaenl.repaint();
            }
        });
        // STAY 버튼 기능
        stayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hitbutton.setEnabled(false);
                stayButton.setEnabled(false);

                while (dealerSum < 17){
                    Card card = deck.remove(deck.size() -1);
                    dealerSum += card.getValue();
                    dealerAceCount +=card.isAce()? 1: 0 ;
                    dealerHand.add(card);
                }
                gamePaenl.repaint();
            }


        });

        gamePaenl.repaint();
    }

    // Card클래스로 카드 deck 만들기
    public void buildDeck(){
        deck = new ArrayList<Card>();
        String[] values = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
        String[] types = {"C", "D", "H", "S"};

        // deck 만들기
        for(int i = 0; i < types.length; i++){
            for(int j = 0; j < values.length; j++){
                Card card = new Card(values[j], types[i]);
                deck.add(card);
            }
        }
        System.out.println("Build Deck");
        System.out.println(deck);
    }

    // 덱 섞기
    public void shuffleDeck(){
        for (int i = 0; i< deck.size(); i++){
            int j = random.nextInt(deck.size());
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);

            deck.set(i,randomCard);
            deck.set(j,currCard);
        }
        System.out.println("AFTER SHUFFLE");
        System.out.println(deck);
    }


    // 플레이어의 카드값이 21이초과하는 경우 ACE카드를 1로 계산한다.
    public int reducePlayerAce(){
        while(playerSum > 21 && playerAceCount > 0){
            playerSum -= 10;
            playerAceCount -=1;
        }
        return  playerSum;
    }

    // 딜러의 카드값이 21이초과하는 경우 ACE카드를 1로 계산한다.
    public int reduceDealerAce(){
        while(dealerSum > 21 && dealerAceCount > 0){
            dealerSum -= 10;
            dealerAceCount -= 1;
        }
        return dealerSum;
    }

}
