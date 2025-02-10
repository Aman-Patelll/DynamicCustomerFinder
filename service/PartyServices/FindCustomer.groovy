import org.moqui.context.ExecutionContext
import org.moqui.entity.EntityCondition
import org.moqui.entity.EntityFind
import org.moqui.entity.EntityList
import org.moqui.entity.EntityValue

ExecutionContext ec = context.ec

EntityFind ef = ec.entity.find("moqui.FindCustomerView").distinct(true);

if (partyId) {
    ef.condition (
            ec.entity.conditionFactory.makeCondition(
                    "partyId", EntityCondition.LIKE, "%" + partyId + "%"
            ).ignoreCase()
    )
}

if(emailAddress) {
    ef.condition("contactMechPurposeEnumId", "EmailPrimary")
    ef.condition("emailAddress", emailAddress)
}

if (combinedName) {
    String fName = combinedName
    String lName = combinedName

    if (combinedName.contains(" ")) {
        fName = combinedName.substring(0, combinedName.indexOf(" "))
        lName = combinedName.indexOf(combinedName.indexOf(" ") + 1)
    }

    cnConditionList = [
            ec.entity.conditionFactory.makeCondition(
                    "organizationName", EntityCondition.LIKE, "%" + combinedName + "%"
            ).ignoreCase(),
            ec.entity.conditionFactory.makeCondition(
                    "firstName", EntityCondition.LIKE, "%" + fName + "%"
            ).ignoreCase(),
            ec.entity.conditionFactory.makeCondition(
                    "lastName", EntityCondition.LIKE, "%" + lName + "%"
            ).ignoreCase()
    ]

    ef.condition (
            ec.entity.conditionFactory.makeCondition (
                    cnConditionList, EntityCondition.OR
            )
    )
}

if (firstName) {
    ef.condition (
            ec.entity.conditionFactory.makeCondition (
                    "firstName", EntityCondition.LIKE, "%" + firstName + "%"
            ).ignoreCase()
    )
}

if (lastName) {
    ef.condition (
            ec.entity.conditionFactory.makeCondition (
                    "lastName", EntityCondition.LIKE, "%" + lastName + "%"
            ).ignoreCase()
    )
}

if (contactNumber) {
    ef.condition (
            ec.entity.conditionFactory.makeCondition (
                    "contactNumber", EntityCondition.LIKE, "%" + contactNumber + "%"
            ).ignoreCase()
    )
}

if (toName) {
    ef.condition (
            ec.entity.conditionFactory.makeCondition (
                    "toName", EntityCondition.LIKE, "%" + toName + "%"
            ).ignoreCase()
    )
}

if (attnName) {
    ef.condition (
            ec.entity.conditionFactory.makeCondition (
                    "attnName", EntityCondition.LIKE, "%" + attnName + "%"
            ).ignoreCase()
    )
}

if (address) {
    cnConditionList = [
            ec.entity.conditionFactory.makeCondition(
                    "address1", EntityCondition.LIKE, "%" + address + "%"
            ).ignoreCase(),
            ec.entity.conditionFactory.makeCondition(
                    "address2", EntityCondition.LIKE, "%" + address + "%"
            ).ignoreCase()
    ]

    ef.condition (
            ec.entity.conditionFactory.makeCondition (
                    cnConditionList, EntityCondition.OR
            )
    )
}

if (city) {
    ef.condition (
            ec.entity.conditionFactory.makeCondition (
                    "city", EntityCondition.LIKE, "%" + city + "%"
            ).ignoreCase()
    )
}

if (postalCode) {
    ef.condition (
            ec.entity.conditionFactory.makeCondition (
                    "postalCode", EntityCondition.LIKE, "%" + postalCode + "%"
            ).ignoreCase()
    )
}

ef.orderBy("organizationName, firstName, lastName")

if (!pageNoLimit) {
    ef.offset(pageIndex as int, pageSize as int);
    ef.limit(pageSize as int)
}

customerList = []
EntityList el = ef.list()

for (EntityValue ev in el)
    customerList.add(ev.partyId)

customerIdListCount = ef.count()
customerIdListPageIndex = ef.pageIndex
customerIdListPageSize = ef.pageSize
customerIdListPageRangeLow = customerIdListPageIndex * customerIdListPageSize + 1
customerIdListPageRangeHigh = (customerIdListPageIndex * customerIdListPageSize) + customerIdListPageSize
if (customerIdListPageRangeHigh > customerIdListCount)
    customerIdListPageRangeHigh = customerIdListCount