
package ws.blackjack;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for card complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="card">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rank" type="{http://blackjack.ws/}rank" minOccurs="0"/>
 *         &lt;element name="suit" type="{http://blackjack.ws/}suit" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "card", propOrder = {
    "rank",
    "suit"
})
public class Card {

    protected Rank rank;
    protected Suit suit;

    /**
     * Gets the value of the rank property.
     * 
     * @return
     *     possible object is
     *     {@link Rank }
     *     
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Sets the value of the rank property.
     * 
     * @param value
     *     allowed object is
     *     {@link Rank }
     *     
     */
    public void setRank(Rank value) {
        this.rank = value;
    }

    /**
     * Gets the value of the suit property.
     * 
     * @return
     *     possible object is
     *     {@link Suit }
     *     
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Sets the value of the suit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Suit }
     *     
     */
    public void setSuit(Suit value) {
        this.suit = value;
    }

}
