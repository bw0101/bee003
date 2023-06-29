package cashcard;


import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CashCardJsonTest {

    @Autowired
    private JacksonTester<CashCard> json;

    @Autowired
    private JacksonTester<CashCard[]> jsonList;

    private CashCard[] cashCards;

    @BeforeEach
    void setUp() {
        cashCards = Arrays.array(
                new CashCard(77L, 999.42),
                new CashCard(99L, 123.45),
                new CashCard(100L, 1.00),
                new CashCard(101L, 150.00));
    }


    @Test
    public void cashCardSerializationTest() throws IOException {
        //CashCard cashCard = new CashCard(77L, 999.42);
        CashCard cashCard = cashCards[0];
        assertThat(json.write(cashCard)).isStrictlyEqualToJson("single.json");
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.id")
                .isEqualTo(77);
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.amount")
                .isEqualTo(999.42);
    }

    @Test
    public void cashCardDeserializationTest() throws IOException {
        String expected = """
                {
                    "id": 77,
                    "amount": 999.42
                }
                """;
        //noinspection AssertBetweenInconvertibleTypes
        assertThat(json.parse(expected)).isEqualTo(new CashCard(77L, 999.42));
        assertThat(json.parseObject(expected).id()).isEqualTo(77);
        assertThat(json.parseObject(expected).amount()).isEqualTo(999.42);
    }

    @Test
    void cashCardListSerializationTest() throws IOException {
        assertThat(jsonList.write(cashCards)).isStrictlyEqualToJson("list.json");
    }

    @Test
    void cashCardListDeserializationTest() throws IOException {
        String expected="""
            [
                { "id": 77, "amount": 999.42 },
                { "id": 99, "amount": 123.45 },
                { "id": 100, "amount": 1.00 },
                { "id": 101, "amount": 150.00 }
            ]
            """;
        //noinspection AssertBetweenInconvertibleTypes
        assertThat(jsonList.parse(expected)).isEqualTo(cashCards);
    }
}
