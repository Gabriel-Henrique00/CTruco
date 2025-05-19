package com.luciano.bonelli.zecatatubot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ZecaTatuBotTest {
    private ZecaTatuBot zecaTatuBot;
    private GameIntel intel;

    @BeforeEach
    public void setup() {
        zecaTatuBot = new ZecaTatuBot();
        intel = mock(GameIntel.class);
    }

    @Nested
    @DisplayName("Test getMaoDeOnzeResponse method")
    class GetMaoDeOnzeResponseTest {

        @Test
        @DisplayName("Opponent has 2 points or less")
        void whenOpponentScoreIsLessOrEqual2() {
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)));
            when(intel.getOpponentScore()).thenReturn(2);
            boolean response = zecaTatuBot.getMaoDeOnzeResponse(intel);
            assertThat(response).isTrue();
        }

        @Test
        @DisplayName("Opponent has 2 points or less and we have bad hand")
        void whenOpponentScoreIsLessOrEqual2AndWeHaveBadHand() {
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.SPADES));
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES)));
            when(intel.getOpponentScore()).thenReturn(1);
            boolean response = zecaTatuBot.getMaoDeOnzeResponse(intel);
            assertThat(response).isFalse();
        }

        @Test
        @DisplayName("Opponent has 4 points or less")
        void whenOpponentScoreIsLessOrEqual4() {
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS));
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS)));
            when(intel.getOpponentScore()).thenReturn(3);
            boolean response = zecaTatuBot.getMaoDeOnzeResponse(intel);
            assertThat(response).isTrue();
        }

        @Test
        @DisplayName("Opponent has 6 points or less")
        void whenOpponentScoreIsLessOrEqual6() {
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS)));
            when(intel.getOpponentScore()).thenReturn(5);
            boolean response = zecaTatuBot.getMaoDeOnzeResponse(intel);
            assertThat(response).isTrue();
        }

        @Test
        @DisplayName("Opponent has 8 points or less")
        void whenOpponentScoreIsLessOrEqual8() {
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)));
            when(intel.getOpponentScore()).thenReturn(8);
            boolean response = zecaTatuBot.getMaoDeOnzeResponse(intel);
            assertThat(response).isTrue();
        }
        @Test
        @DisplayName("Opponent has 10 points or less")
        void whenOpponentScoreIsLessOrEqual10() {
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)));
            when(intel.getOpponentScore()).thenReturn(10);
            boolean response = zecaTatuBot.getMaoDeOnzeResponse(intel);
            assertThat(response).isTrue();
        }

        @Test
        @DisplayName("Opponent has 10 points or less and we have 2 manilhas")
        void whenOpponentScoreIsLessOrEqual10With2Manilhas() {
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)));
            when(intel.getOpponentScore()).thenReturn(10);
            boolean response = zecaTatuBot.getMaoDeOnzeResponse(intel);
            assertThat(response).isTrue();
        }

    }

    @Nested
    @DisplayName("Test decideIfRaises method")
    class DecideIfRaisesTest {
        @Test
        @DisplayName("First round with hand value < 7")
        void whenFirstRoundAndHandValueLessThan10() {
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));
            boolean result = zecaTatuBot.decideIfRaises(intel);
            assertThat(result).isTrue();
        }
        @Test
        @DisplayName("Second round after first round was a draw")
        void whenSecondRoundAndFirstRoundWasDraw() {
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS));
            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.DREW));

            boolean result = zecaTatuBot.decideIfRaises(intel);
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Second round, lost first, has 2 manilhas")
        void whenSecondRoundLostFirstWith2Manilhas() {
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
            boolean result = zecaTatuBot.decideIfRaises(intel);
            assertThat(result).isTrue();
        }
        @Test
        @DisplayName("Second round with handValue > 16")
        void whenSecondRoundAndHandValueAbove16() {
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));
            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
            boolean result = zecaTatuBot.decideIfRaises(intel);
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Third round with at least one manilha")
        void whenThirdRoundAndHasManilha() {
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS));
            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST, GameIntel.RoundResult.DREW));
            boolean result = zecaTatuBot.decideIfRaises(intel);
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("First round with hand value < 10")
        void whenFirstRoundAndHandValueLessThan10False() {
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));
            boolean result = zecaTatuBot.decideIfRaises(intel);
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Second round, lost first, only one manilha and handValue <= 16")
        void whenSecondRoundLostFirstWithOneManilhaAndLowHandValue() {
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
            boolean result = zecaTatuBot.decideIfRaises(intel);
            assertThat(result).isFalse();
        }

    }

    @Nested
    @DisplayName("Test roundCheck method")
    class RoundCheckTest {

        @Test
        @DisplayName("Should return 'Round 1' when 3 cards in hand")
        void whenThreeCards() {
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            ));
            assertThat(zecaTatuBot.roundCheck(intel)).isEqualTo("Round 1");
        }

        @Test
        @DisplayName("Should return 'Round 2' when 2 cards in hand")
        void whenTwoCards() {
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
            ));
            assertThat(zecaTatuBot.roundCheck(intel)).isEqualTo("Round 2");
        }

        @Test
        @DisplayName("Should return 'Round 3' when 1 card in hand")
        void whenOneCard() {
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES)
            ));
            assertThat(zecaTatuBot.roundCheck(intel)).isEqualTo("Round 3");
        }

        @Test
        @DisplayName("Should return 'No cards' when 0 cards in hand")
        void whenNoCards() {
            when(intel.getCards()).thenReturn(List.of());
            assertThat(zecaTatuBot.roundCheck(intel)).isEqualTo("No cards");
        }

    }


    @Nested
    @DisplayName("Test chooseCard method")
    class ChooseCardTest {

        @Test
        @DisplayName("3 cards with Zap and Copas - should play worst card")
        void whenHasZapAndCopasShouldPlayWorst() {
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
            ));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES));

            CardToPlay cardToPlay = zecaTatuBot.chooseCard(intel);
            assertThat(cardToPlay.content()).isEqualTo(zecaTatuBot.getLowCard(intel));
        }

        @Test
        @DisplayName("3 cards, opponent played, worst beats opponent - play worst card")
        void worstCardBeatsOpponent() {
            TrucoCard opponent = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
            when(intel.getOpponentCard()).thenReturn(Optional.of(opponent));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES)
            ));

            CardToPlay cardToPlay = zecaTatuBot.chooseCard(intel);
            assertThat(cardToPlay.content()).isEqualTo(zecaTatuBot.getLowCard(intel));
        }

        @Test
        @DisplayName("3 cards, opponent played, low beats opponent - play low")
        void averageCardBeatsOpponent() {
            TrucoCard opponent = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
            when(intel.getOpponentCard()).thenReturn(Optional.of(opponent));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SIX, CardSuit.SPADES));
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES)
            ));

            CardToPlay cardToPlay = zecaTatuBot.chooseCard(intel);
            assertThat(cardToPlay.content()).isEqualTo(zecaTatuBot.getLowCard(intel));
        }

        @Test
        @DisplayName("3 cards, opponent played, best beats opponent - play best")
        void bestCardBeatsOpponent() {
            TrucoCard opponent = TrucoCard.of(CardRank.THREE, CardSuit.HEARTS);
            when(intel.getOpponentCard()).thenReturn(Optional.of(opponent));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS));
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES)
            ));

            CardToPlay cardToPlay = zecaTatuBot.chooseCard(intel);
            assertThat(cardToPlay.content()).isEqualTo(zecaTatuBot.getHighCard(intel));
        }

        @Test
        @DisplayName("3 cards, opponent played, no card beats - play worst")
        void noCardBeatsOpponent() {
            TrucoCard opponent = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);
            when(intel.getOpponentCard()).thenReturn(Optional.of(opponent));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
            ));

            CardToPlay cardToPlay = zecaTatuBot.chooseCard(intel);
            assertThat(cardToPlay.content()).isEqualTo(zecaTatuBot.getLowCard(intel));
        }

        @Test
        @DisplayName("2 cards, opponent played, worst beats opponent - play worst")
        void twoCardsWorstBeatsOpponent() {
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
            ));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.SPADES));
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)));

            CardToPlay cardToPlay = zecaTatuBot.chooseCard(intel);
            assertThat(cardToPlay.content()).isEqualTo(zecaTatuBot.getLowCard(intel));
        }

        @Test
        @DisplayName("2 cards, opponent played, best beats opponent - play best card to defeat opponent")
        void twoCardsBestBeatsOpponent() {
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
            ));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.SPADES));
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS)));

            CardToPlay cardToPlay = zecaTatuBot.chooseCard(intel);
            assertThat(cardToPlay.content()).isEqualTo(zecaTatuBot.getLowCard(intel));
        }

        @Test
        @DisplayName("2 cards, opponent played, 1 card beats - play best card")
        void twoCardsNoCardBeatsOpponent() {
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
            ));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES)));

            CardToPlay cardToPlay = zecaTatuBot.chooseCard(intel);
            assertThat(cardToPlay.content()).isEqualTo(zecaTatuBot.getHighCard(intel));
        }

        @Test
        @DisplayName("3 cards - play best card")
        void twoCardsHasManilha() {
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
            ));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));

            CardToPlay cardToPlay = zecaTatuBot.chooseCard(intel);
            assertThat(cardToPlay.content()).isEqualTo(zecaTatuBot.getHighCard(intel));
        }

        @Test
        @DisplayName("2 cards, no manilha or opponent card - play average")
        void twoCardsDefaultNoOpponent() {
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
            ));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS));

            CardToPlay cardToPlay = zecaTatuBot.chooseCard(intel);
            assertThat(cardToPlay.content()).isEqualTo(zecaTatuBot.getLowCard(intel));
        }

        @Test
        @DisplayName("1 card left - play it")
        void oneCardLeft() {
            TrucoCard lastCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
            when(intel.getCards()).thenReturn(List.of(lastCard));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));

            CardToPlay cardToPlay = zecaTatuBot.chooseCard(intel);
            assertThat(cardToPlay.content()).isEqualTo(lastCard);
        }
    }


    @Nested
    @DisplayName("getRaiseResponse")
    class GetRaiseResponseTest {

        @Test
        @DisplayName("Should return 1 when 3 cards and 2 manilhas")
        void threeCardsTwoManilhas() {
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
            ));
            assertThat(zecaTatuBot.getRaiseResponse(intel)).isEqualTo(1);
        }

        @Test
        @DisplayName("Should return 0 when 3 cards, 1 manilha and handValue >= 24")
        void threeCardsOneManilhaHighValue() {
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS)
            ));
            assertThat(zecaTatuBot.getRaiseResponse(intel)).isEqualTo(0);
        }

        @Test
        @DisplayName("Should return -1 when 3 cards and no manilha and low hand value")
        void threeCardsNoManilhaLowValue() {
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
            ));
            assertThat(zecaTatuBot.getRaiseResponse(intel)).isEqualTo(-1);
        }

        @Test
        @DisplayName("Should return 1 when 2 cards and both are manilhas")
        void twoCardsTwoManilhas() {
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES)
            ));
            assertThat(zecaTatuBot.getRaiseResponse(intel)).isEqualTo(1);
        }

        @Test
        @DisplayName("Should return 0 when 2 cards and 1 is manilha")
        void twoCardsOneManilha() {
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.SPADES));
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS), // manilha
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
            ));
            assertThat(zecaTatuBot.getRaiseResponse(intel)).isEqualTo(0);
        }

        @Test
        @DisplayName("Should return 0 when 2 cards and handValue >= 17")
        void twoCardsHighValueNoManilha() {
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS), // forte
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES)   // forte
            ));
            assertThat(zecaTatuBot.getRaiseResponse(intel)).isEqualTo(0);
        }

        @Test
        @DisplayName("Should return 1 when 1 card and handValue >= 12")
        void oneCardHighValue() {
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));
            when(intel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS) // manilha
            ));
            assertThat(zecaTatuBot.getRaiseResponse(intel)).isEqualTo(1);
        }
    }


    @Test
    @DisplayName("CountHowManyManilhasInHand")
    public void countManilhas() {
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));
        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES)));
        assertThat(zecaTatuBot.countManilha(intel)).isEqualTo(2);
    }
    @Test
    @DisplayName("ShoulCountHandValue")
    public void handValueManilha() {
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));
        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)));
        assertThat(zecaTatuBot.handValue(intel)).isEqualTo(23);
    }

    @Test
    @DisplayName("ShouldTellHighestCardInHand")
    public void HighestCard() {
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS));
        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SIX, CardSuit.SPADES)));
        assertThat(zecaTatuBot.getHighCard(intel)).isEqualTo(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
    }

    @Test
    @DisplayName("ShouldTellHighestCardInHandWith2equalsCard")
    public void HighestCardWith2Equals() {
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS));
        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SIX, CardSuit.SPADES)));
        assertThat(zecaTatuBot.getHighCard(intel).getRank()).isEqualTo((CardRank.TWO));
    }

    @Test
    @DisplayName("ShouldTellLowestCardInHand")
    public void LowestCard() {
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES)));
        assertThat(zecaTatuBot.getLowCard(intel)).isEqualTo(TrucoCard.of(CardRank.FIVE, CardSuit.SPADES));
    }

    @Test
    @DisplayName("ShouldTellMidCardInHand")
    public void MidCard() {
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));
        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)));
        assertThat(zecaTatuBot.getMidCard(intel)).isEqualTo(TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS));
    }

    @Test
    @DisplayName("Returns true when first round was won")
    void shouldReturnTrueIfFirstRoundWasWon() {
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
        boolean result = zecaTatuBot.wonFirstRound(intel);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Returns true when first round was drew")
    void shouldReturnTrueIfFirstRoundWasDrew() {
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.DREW));
        boolean result = zecaTatuBot.drewFirstRound(intel);
        assertThat(result).isTrue();
    }



}

