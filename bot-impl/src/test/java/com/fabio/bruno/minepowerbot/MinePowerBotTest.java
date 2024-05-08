package com.fabio.bruno.minepowerbot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static com.fabio.bruno.minepowerbot.MinePowerBotIntelMockBuilder.create;
import static org.mockito.Mockito.when;

class MinePowerBotTest {

    private MinePowerBot sut;
    private GameIntel intel;

    @BeforeEach
    void setUp() {
        sut = new MinePowerBot();
    }

    @Test
    @DisplayName("When winning and playing the first card, should play a weak one")
    void playingFirstCard() {
        intel = create()
                .scoreMine(1)
                .viraToBe(CardRank.KING, CardSuit.SPADES)
                .cards(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS), TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS))
                .finish();
        when(intel.getOpponentCard()).thenReturn(Optional.empty());

        assertThat(sut.getLowerCard(intel)).isEqualTo(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
    }

    @Test
    @DisplayName("Test when the bot has the lowest possible score (0)")
    void testBotWithLowestScore() {
        // Given
        intel = MinePowerBotIntelMockBuilder.create()
                .scoreMine(0) // Bot's score is 0
                .scoreOponent(0) // Opponent's score is also 0
                .cards(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS), // Example hand
                        TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS))
                .viraToBeDiamondsOfRank(CardRank.FOUR)
                .opponentCardToBe(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS))
                .finish();

        // When
        boolean botRaises = sut.decideIfRaises(intel);

        // Then
        assertThat(botRaises).isFalse(); // Bot should not raise with the lowest score
    }


    @Test
    @DisplayName("Should return false if does not have manilha")
    void shouldReturnFalseIfDoNotHaveManilha() {
        intel = create().viraToBe(CardRank.FOUR, CardSuit.SPADES).cards(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS), TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS), TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)).finish();
        TrucoCard vira = intel.getVira();

        assertThat(sut.chooseCard(intel).content().isManilha(vira)).isFalse();
    }

    @Test
    @DisplayName("Should play the lowest rank manilha if it has at least two of them in hand")
    void shouldPlayLowestRankManilhaIfItHasAtLeastTwoOfThemInHandInTheFirstRoundAndItIsTheFirstToPlay() {
        intel = create().viraToBe(CardRank.KING, CardSuit.DIAMONDS).cards(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS), // Manilha
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), // Manilha
                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)).finish();

        assertThat(sut.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
    }

    @Test
    @DisplayName("Should raise bet if has manilha in hand")
    void raiseIfHasManilha() {
        intel = create().viraToBe(CardRank.KING, CardSuit.DIAMONDS).cards(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS), TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)).finish();

        assertThat(sut.decideIfRaises(intel)).isTrue();
    }

    @Test
    @DisplayName("Should play the manilha in the first round if it has only one and if it is the first to play")
    void shouldPlayTheManilhaInTheFirstRoundIfItIsTheOnlyOneAndIfItIsTheFirstToPlay(){
        intel = create().viraToBe(CardRank.QUEEN, CardSuit.CLUBS)
                .cards(TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                TrucoCard.of(CardRank.SIX, CardSuit.SPADES)).finish();
        when(intel.getOpponentCard()).thenReturn(Optional.empty());

        assertThat(sut.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS));
    }

    @Test
    @DisplayName("Check if has Zap")
    void testHasZap(){
        intel = create().viraToBe(CardRank.QUEEN, CardSuit.CLUBS)
                .cards(TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.SIX, CardSuit.SPADES)).finish();


        assertThat(sut.hasZap(intel)).isTrue();
    }


    @Test
    @DisplayName("Should raise if bot has two cards above rank two")
    void shouldRaiseIfHasTwoCardsAboveRankTwo() {
        intel = create().viraToBe(CardRank.QUEEN, CardSuit.CLUBS).cards(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.JACK, CardSuit.SPADES), // manilha
                TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)).opponentCardToBe(TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)).finish();

        assertThat(sut.decideIfRaises(intel)).isTrue();
    }

    @Test
    @DisplayName("Should play the lowest card that is stronger than the opponent card")
    void shouldPlayTheLowestCardThatIsStrongerThanOpponentCard() {
        intel = create().viraToBe(CardRank.ACE, CardSuit.SPADES).cards(
                TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS))
                .opponentCardToBe(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)).finish();

        assertThat(sut.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));
    }

    @Test
    @DisplayName("Should not raise bet when is mao de onze.")
    void shouldNotRaiseBetWhenIsMaoDeOnze() {
        intel = create().finish();
        when(intel.getScore()).thenReturn(11);
        assertThat(sut.decideIfRaises(intel)).isFalse();
    }

    @Test
    @DisplayName("Should respond raise if has strong cards")
    void shouldRespondRaiseIfHasStrongCards() {
        intel = create()
                .viraToBe(CardRank.TWO, CardSuit.CLUBS)
                .cards(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        TrucoCard.of(CardRank.THREE, CardSuit.CLUBS))
                .finish();

        assertThat(sut.getRaiseResponse(intel)).isEqualTo(1);
    }

    @Test
    @DisplayName("Should respond raise if has zap or two manilhas")
    void shouldRespondRaiseIfHasZapOrTwoManilhas() {
        intel = create()
                .viraToBe(CardRank.THREE, CardSuit.CLUBS)
                .cards(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                        TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS))
                .finish();

        assertThat(sut.getRaiseResponse(intel)).isEqualTo(0);
    }

    @Test
    @DisplayName("Should respond raise with quit")
    void shouldRespondRaiseWithQuit() {
        intel = create()
                .viraToBe(CardRank.FIVE, CardSuit.CLUBS)
                .cards(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                        TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS))
                .finish();

        assertThat(sut.getRaiseResponse(intel)).isEqualTo(-1);
    }

    @ParameterizedTest
    @CsvSource({"3", "4", "5"})
    @DisplayName("Should ask for a point raise if opponent score is equal or less than the threshold.")
    void shouldRaiseIfOpponentScoreIsEqualOrLessThanThreshold(int opponentScore) {
        intel = create().scoreOponent(opponentScore).finish();
        when(intel.getOpponentScore()).thenReturn(opponentScore);
        assertThat(sut.decideIfRaises(intel)).isTrue();
    }

    @ParameterizedTest
    @CsvSource({"6", "7", "8"})
    @DisplayName("Should not ask for a point raise if opponent score is greater than the threshold.")
    void shouldNotRaiseIfOpponentScoreIsGreaterThanThreshold(int opponentScore) {
        intel = create().scoreOponent(opponentScore).finish();
        when(intel.getOpponentScore()).thenReturn(opponentScore);
        assertThat(sut.decideIfRaises(intel)).isFalse();
    }

    @ParameterizedTest
    @CsvSource({"4, 0", "9, 5", "10, 6"})
    @DisplayName("Will raise bet when winning by 3+ points difference.")
    void shouldRaiseIfIsWinningByThreePoints(int botScore, int opponentScore) {
        intel = create().scoreOponent(opponentScore).scoreMine(botScore).finish();
        when(intel.getOpponentScore()).thenReturn(opponentScore);
        assertThat(sut.decideIfRaises(intel)).isTrue();
    }

    @ParameterizedTest
    @CsvSource({"8, 6", "9, 7", "7, 8"})
    @DisplayName("Will not raise bet when not winning by 3+ points difference.")
    void shouldNotRaiseIfIsWinningByThreePoints(int botScore, int opponentScore) {
        intel = create().scoreOponent(opponentScore).scoreMine(botScore).finish();
        when(intel.getOpponentScore()).thenReturn(opponentScore);
        assertThat(sut.decideIfRaises(intel)).isFalse();
    }

    @Test
    @DisplayName("Should raise if bot score and opponent score are both 9 and our bot has a special card.")
    void shouldRaiseIfBothBotsScoresAre9AndOurBotHasSpecialCard() {
        intel = create().scoreMine(9).scoreOponent(9).cards(
                        TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS))
                .viraToBeDiamondsOfRank(CardRank.FOUR)
                .finish();

        assertThat(sut.decideIfRaises(intel)).isTrue();
    }

    @Test
    @DisplayName("Should not raise if bot score and opponent score are both 9 and our bot has no special card.")
    void shouldNotRaiseIfBothBotsScoresAre9AndOurBotHasNoSpecialCard() {
        intel = create().scoreMine(9).scoreOponent(9).cards(
                        TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS))
                .viraToBeDiamondsOfRank(CardRank.FOUR)
                .finish();

        assertThat(sut.decideIfRaises(intel)).isFalse();
    }

    @Test
    @DisplayName("Should play the highest card when tied")
    void shouldPlayTheHighestCardWhenTied() {
        intel = create().scoreMine(6).scoreOponent(6).cards(
                        TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS))
                .viraToBeDiamondsOfRank(CardRank.FOUR)
                .opponentCardToBe(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS))
                .finish();

        assertThat(sut.chooseCard(intel).content().equals(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS))).isTrue();
    }
}