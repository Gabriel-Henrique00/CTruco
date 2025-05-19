/*
 *  Copyright (C) 2025 Vitor A. de Jesus - IFSP/SCL
 *  Contact: vitor <dot> aguiar <at> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

package com.aguiar.vitor.trucorinthians;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TrucorinthiansTest {

    private Trucorinthians sut;
    private GameIntel intel;

    @BeforeEach
    void setUp() {
        sut = new Trucorinthians();
    }

    @Nested
    @DisplayName("getMaoDeOnzeResponse Tests")
    class GetMaoDeOnzeResponseTest {
        @DisplayName("Should accept playing mão de onze if has at least one manilha")
        @Test
        void shouldAcceptIfHasManilha() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

            TrucoCard manilha = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);
            TrucoCard fraca1 = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);
            TrucoCard fraca2 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 3)
                    .botInfo(List.of(fraca1, manilha, fraca2), 11)
                    .opponentScore(10)
                    .build();

            boolean result = sut.getMaoDeOnzeResponse(intel);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Should decline playing mão de onze if has no manilha")
        void shouldDeclineIfHasNoManilha() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

            TrucoCard fraca1 = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);
            TrucoCard fraca2 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
            TrucoCard fraca3 = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 3)
                    .botInfo(List.of(fraca1, fraca2, fraca3), 11)
                    .opponentScore(10)
                    .build();

            boolean result = sut.getMaoDeOnzeResponse(intel);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Should accept playing mão de onze if both players have 11 points")
        void shouldAcceptIfBothHave11Points() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

            TrucoCard fraca1 = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);
            TrucoCard fraca2 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
            TrucoCard fraca3 = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 3)
                    .botInfo(List.of(fraca1, fraca2, fraca3), 11)
                    .opponentScore(11)
                    .build();

            boolean result = sut.getMaoDeOnzeResponse(intel);

            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("decideIfRaises Tests")
    class DecideIfRaisesTest {

        @Test
        @DisplayName("Should raise if has two manilhas in first round")
        void shouldRaiseIfHasTwoManilhasInFirstRound() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

            TrucoCard manilha1 = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);
            TrucoCard manilha2 = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);
            TrucoCard fraca = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(List.of(manilha1, fraca, manilha2), 0)
                    .opponentScore(0)
                    .build();

            boolean result = sut.decideIfRaises(intel);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Should not raise if has less than two manilhas in first round")
        void shouldNotRaiseIfHasLessThanTwoManilhasInFirstRound() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

            TrucoCard manilha = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);
            TrucoCard media = TrucoCard.of(CardRank.THREE, CardSuit.HEARTS);
            TrucoCard fraca = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(List.of(manilha, media, fraca), 0)
                    .opponentScore(0)
                    .build();

            boolean result = sut.decideIfRaises(intel);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Should raise in second round if has one manilha and one strong card")
        void shouldRaiseInSecondRoundIfHasManilhaAndStrongCard() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

            TrucoCard manilha = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);
            TrucoCard fraca = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
            TrucoCard forte = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);

            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.DREW);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, List.of(), vira, 1)
                    .botInfo(List.of(manilha, fraca, forte), 2)
                    .opponentScore(3)
                    .build();

            boolean result = sut.decideIfRaises(intel);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Should not raise in second round if has only one manilha and weak cards")
        void shouldNotRaiseInSecondRoundIfHasOnlyManilhaAndWeakCards() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

            TrucoCard manilha = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);
            TrucoCard fraca1 = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
            TrucoCard fraca2 = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.DREW);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, List.of(), vira, 1)
                    .botInfo(List.of(fraca1, manilha, fraca2), 2)
                    .opponentScore(3)
                    .build();

            boolean result = sut.decideIfRaises(intel);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Should raise in third round if has a card with force ≥ 8")
        void shouldRaiseInThirdRoundIfHasVeryStrongCard() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

            List<GameIntel.RoundResult> roundResults = List.of(
                    GameIntel.RoundResult.LOST,
                    GameIntel.RoundResult.WON
            );

            TrucoCard forte = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);
            TrucoCard fraca1 = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);
            TrucoCard fraca2 = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, List.of(), vira, 3)
                    .botInfo(List.of(fraca1, forte, fraca2), 10)
                    .opponentScore(10)
                    .build();

            boolean result = sut.decideIfRaises(intel);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Should not raise in third round if has no strong cards (force < 8)")
        void shouldNotRaiseInThirdRoundIfHasNoStrongCards() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

            List<GameIntel.RoundResult> roundResults = List.of(
                    GameIntel.RoundResult.DREW,
                    GameIntel.RoundResult.WON
            );

            TrucoCard fraca1 = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);       // força 1
            TrucoCard fraca2 = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);    // força 2
            TrucoCard fraca3 = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);     // força 3

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, List.of(), vira, 3)
                    .botInfo(List.of(fraca1, fraca2, fraca3), 9)
                    .opponentScore(10)
                    .build();

            boolean result = sut.decideIfRaises(intel);

            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("chooseCard Tests")
    class ChooseCardTest {

        @DisplayName("Shouldn't return null when choosing card")
        @Test
        void shouldNotReturnNullWhenChoosingCard() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), 1)
                    .botInfo(List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)), 0)
                    .opponentScore(0)
                    .build();

            CardToPlay result = sut.chooseCard(intel);

            assertThat(result).isNotNull();
            assertThat(result.content()).isNotNull();
        }

        @Nested
        class FirstRoundTest {
            @DisplayName("Should play weakest card when starting first round")
            @Test
            void shouldPlayWeakestCardWhenFirstRoundAsFirstPlayer() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

                TrucoCard fraca = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);
                TrucoCard media = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);
                TrucoCard forte = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), List.of(), vira, 1)
                        .botInfo(List.of(forte, fraca, media), 0)
                        .opponentScore(0)
                        .build();

                CardToPlay result = sut.chooseCard(intel);

                assertThat(result.content()).isEqualTo(fraca);
            }

            @DisplayName("Should play smallest non-losing card when responding in first round")
            @Test
            void shouldPlaySmallestCardThatDoesNotLoseWhenFirstRoundAsSecondPlayer() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

                TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

                TrucoCard perde = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
                TrucoCard empata = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
                TrucoCard vence = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);

                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), List.of(), vira, 1)
                        .botInfo(List.of(perde, empata, vence), 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard)
                        .build();

                CardToPlay result = sut.chooseCard(intel);

                assertThat(result.content()).isEqualTo(empata);
            }

            @DisplayName("Should play weakest card if cannot win or draw when responding in first round")
            @Test
            void shouldPlayWeakestCardIfCannotWinOrDrawWhenFirstRoundAsSecondPlayer() {
                TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);
                TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);

                TrucoCard fraca = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);
                TrucoCard media = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
                TrucoCard alta = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);

                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), List.of(), vira, 1)
                        .botInfo(List.of(media, fraca, alta), 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard)
                        .build();

                CardToPlay result = sut.chooseCard(intel);

                assertThat(result.content()).isEqualTo(fraca);
            }

            @DisplayName("Should play the weakest winning card in first round if losing the score and responding")
            @Test
            void shouldPlayWeakestWinningCardIfLosingScoreWhenFirstRoundAndPlayingSecond() {
                TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

                TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.HEARTS);

                TrucoCard vence1 = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);
                TrucoCard vence2 = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);
                TrucoCard empata = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), List.of(), vira, 1)
                        .botInfo(List.of(vence1, empata, vence2), 3)
                        .opponentScore(8)
                        .opponentCard(opponentCard)
                        .build();

                CardToPlay result = sut.chooseCard(intel);

                assertThat(result.content()).isEqualTo(vence2);
            }
        }

        @Nested
        class SecondRoundTest {
            @DisplayName("Should play strongest card in second round after losing first")
            @Test
            void shouldPlayStrongestCardWhenSecondRoundAfterLosingFirst() {
                TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);

                List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.LOST);

                TrucoCard fraca = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);
                TrucoCard media = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
                TrucoCard manilha = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);

                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(roundResults, List.of(), vira, 1)
                        .botInfo(List.of(fraca, manilha, media), 0)
                        .opponentScore(0)
                        .build();

                CardToPlay result = sut.chooseCard(intel);

                assertThat(result.content()).isEqualTo(manilha);
            }

            @DisplayName("Should play weakest card when second round after winning first if starting round")
            @Test
            void shouldPlayWeakestCardWhenSecondRoundAfterWinningFirstAsFirstToPlay() {
                TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

                List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON);

                TrucoCard fraca = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
                TrucoCard media = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
                TrucoCard manilha = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);

                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(roundResults, List.of(), vira, 1)
                        .botInfo(List.of(media, manilha, fraca), 0)
                        .opponentScore(0)
                        .build();

                CardToPlay result = sut.chooseCard(intel);

                assertThat(result.content()).isEqualTo(fraca);
            }

            @DisplayName("Should play smallest non-losing card after winning first when responding in second round")
            @Test
            void shouldPlaySmallestCardThatDoesNotLoseWhenSecondAfterWinnigFirstAsSecondPlayer() {
                TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

                List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON);

                TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);

                TrucoCard fraca = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
                TrucoCard empata = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);
                TrucoCard vence = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);

                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(roundResults, List.of(), vira, 1)
                        .botInfo(List.of(vence, empata, fraca), 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard)
                        .build();

                CardToPlay result = sut.chooseCard(intel);

                assertThat(result.content()).isEqualTo(empata);
            }

            @DisplayName("Should play strongest card when second round after draw as first to play")
            @Test
            void shouldPlayStrongestCardWhenSecondRoundAfterDrawAsFirstToPlay() {
                TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

                List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.DREW);

                TrucoCard fraca = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
                TrucoCard media = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);
                TrucoCard manilha = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);

                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(roundResults, List.of(), vira, 1)
                        .botInfo(List.of(fraca, media, manilha), 0)
                        .opponentScore(0)
                        .build();

                CardToPlay result = sut.chooseCard(intel);

                assertThat(result.content()).isEqualTo(manilha);
            }

            @DisplayName("Should play smallest non-losing card after draw first when responding in second round")
            @Test
            void shouldPlaySmallestCardThatDoesNotLosesWhenSecondRoundAfterDrawAsSecondToPlay() {
                TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

                List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.DREW);

                TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

                TrucoCard fraca = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
                TrucoCard empata = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);
                TrucoCard vence = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);

                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(roundResults, List.of(), vira, 1)
                        .botInfo(List.of(fraca, vence, empata), 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard)
                        .build();

                CardToPlay result = sut.chooseCard(intel);

                assertThat(result.content()).isEqualTo(empata);
            }

            @DisplayName("Should play strongest card if losing score when starting in second round")
            @Test
            void shouldPlayStrongestCardIfLosingScoreWhenSecondRoundAsFirstPlayer() {
                TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

                List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON);

                TrucoCard fraca = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
                TrucoCard media = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
                TrucoCard forte = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);

                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(roundResults, List.of(), vira, 1)
                        .botInfo(List.of(fraca, media, forte), 4)
                        .opponentScore(8)
                        .build(); // primeiro a jogar

                CardToPlay result = sut.chooseCard(intel);

                assertThat(result.content()).isEqualTo(forte);
            }

            @DisplayName("Should prioritize winning over drawing when in second round, losing the score, and playing second")
            @Test
            void shouldPrioritizeWinningOverDrawingWhenSecondRoundIfLosingScoreAndPlayingSecond() {
                TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

                List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.DREW);

                TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

                TrucoCard empata = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);
                TrucoCard vence = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);
                TrucoCard fraca = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);

                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(roundResults, List.of(), vira, 1)
                        .botInfo(List.of(empata, fraca, vence), 4)
                        .opponentScore(9)
                        .opponentCard(opponentCard)
                        .build();

                CardToPlay result = sut.chooseCard(intel);

                assertThat(result.content()).isEqualTo(vence);
            }

            @DisplayName("Should play the weakest winning card in second round if won the first but is losing the score and playing second")
            @Test
            void shouldPlayWeakestWinningCardIfWonFirstButLosingScoreWhenSecondRoundAndPlayingSecond() {
                TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

                List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON);

                TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                TrucoCard vence1 = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);
                TrucoCard vence2 = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);
                TrucoCard empata = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(roundResults, List.of(), vira, 1)
                        .botInfo(List.of(vence1, vence2, empata), 6)
                        .opponentScore(9)
                        .opponentCard(opponentCard)
                        .build();

                CardToPlay result = sut.chooseCard(intel);

                assertThat(result.content()).isEqualTo(vence2);
            }
        }

        @Nested
        class ThirdRoundTest {
            @DisplayName("Should play strongest card in third round")
            @Test
            void shouldPlayStrongestCardInThirdRound() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

                List<GameIntel.RoundResult> roundResults = List.of(
                        GameIntel.RoundResult.LOST,
                        GameIntel.RoundResult.WON
                );

                TrucoCard fraca = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
                TrucoCard media = TrucoCard.of(CardRank.THREE, CardSuit.HEARTS);
                TrucoCard manilha = TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS);

                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(roundResults, List.of(), vira, 1)
                        .botInfo(List.of(fraca, manilha, media), 0)
                        .opponentScore(0)
                        .build();

                CardToPlay result = sut.chooseCard(intel);

                assertThat(result.content()).isEqualTo(manilha);
            }
        }
    }

    @Nested
    @DisplayName("getRaiseResponse Tests")
    class GetRaiseResponseTest {
        @Test
        @DisplayName("Should re-raise if has two manilhas")
        void shouldReRaiseIfHasTwoManilhas() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

            TrucoCard manilha1 = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);
            TrucoCard manilha2 = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);
            TrucoCard fraca = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 3)
                    .botInfo(List.of(manilha1, manilha2, fraca), 8)
                    .opponentScore(9)
                    .build();

            int result = sut.getRaiseResponse(intel);

            assertThat(result).isEqualTo(1);
        }

        @Test
        @DisplayName("Should accept raise if has one manilha or card with force ≥ 8")
        void shouldAcceptIfHasOneManilhaOrStrongCard() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

            TrucoCard manilha = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);
            TrucoCard fraca1 = TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS);
            TrucoCard fraca2 = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 3)
                    .botInfo(List.of(fraca1, manilha, fraca2), 9)
                    .opponentScore(9)
                    .build();

            int result = sut.getRaiseResponse(intel);

            assertThat(result).isEqualTo(0);
        }

        @Test
        @DisplayName("Should refuse raise if has only weak cards (force < 8)")
        void shouldRefuseIfHasOnlyWeakCards() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

            TrucoCard fraca1 = TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS);
            TrucoCard fraca2 = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
            TrucoCard fraca3 = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 3)
                    .botInfo(List.of(fraca1, fraca2, fraca3), 10)
                    .opponentScore(9)
                    .build();

            int result = sut.getRaiseResponse(intel);

            assertThat(result).isEqualTo(-1);
        }
    }
}
