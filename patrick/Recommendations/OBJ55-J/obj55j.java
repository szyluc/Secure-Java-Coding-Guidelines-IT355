/**
 * RECOMMENDATION: OBJ55-J
 * DESCRIPTION: Remove short-lived objects from long-lived container objects
 * FIX: Instead of using a dead flag, assign NULL to elements that become irrelevant
 */

class DataElement {
    // fields
}

List<DataElement> longLivedList = new ArrayList<DataElement>();

longLivedList.set(someIndex, NULL);