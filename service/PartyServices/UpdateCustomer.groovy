import org.moqui.context.ExecutionContext
import org.moqui.entity.EntityCondition
import org.moqui.entity.EntityFind
import org.moqui.entity.EntityList
import org.moqui.entity.EntityValue

ExecutionContext ec = context.ec

resultEmail = ec.service
        .sync()
        .name("PartyServices.find#Customer")
        .parameters(["emailAddress": emailAddress])
        .call()

if (resultEmail.get("customerList").size()) {
    if(contactNumber || countryCode || areaCode) {
        partyIdList =  resultEmail.get("customerList")

        for (partyId in partyIdList) {
            EntityFind ef = ec.entity.find("moqui.PartyContactMech")
            ef.condition("contactMechPurposeEnumId", "PhonePrimary")

            EntityList el = ef.list()

            for (EntityValue ev in el) {
                if(ev.isFieldSet("thruDate")) {
                    def currDate = new Date()
                    ev.setAll(context + ["thruDate": currDate]).update()
                }
            }
        }

        EntityValue contactMech = ec.entity
                .makeValue("moqui.ContactMech")
                .setAll(["infoString": emailAddress,
                         "contactMechTypeEnumId": "TELECOM_NUMBER"])
                .setSequencedIdPrimary()
                .create()

        ec.entity
                .makeValue("moqui.TelecomNumber")
                .setAll(["contactMechId": contactMech.contactMechId,
                         "countryCode": countryCode,
                         "areaCode": areaCode,
                         "contactNumber": contactNumber])
                .create()

        def currDate = new Date()
        ec.entity
                .makeValue("moqui.PartyContactMech")
                .setAll(context + ["partyId" : resultEmail.get("customerList").get(0),
                                   "contactMechId": contactMech.contactMechId,
                                   "contactMechPurposeEnumId" : "PhonePrimary",
                                   "fromDate": currDate])
                .create()

    }

    if(toName || attnName || address1 || address2 || city || postalCode) {
        partyIdList =  resultEmail.get("customerList")
        for (partyId in partyIdList) {
            EntityFind ef = ec.entity.find("moqui.PartyContactMech")
            ef.condition("contactMechPurposeEnumId", "PostalPrimary")

            EntityList el = ef.list()


            for (EntityValue ev in el) {
                if(ev.isFieldSet("thruDate") ) {
                    def currDate = new Date()
                    ev.setAll(context + ["thruDate": currDate]).update()
                }
            }
        }

        EntityValue contactMech = ec.entity
                .makeValue("moqui.ContactMech")
                .setAll(["infoString": emailAddress,
                         "contactMechTypeEnumId": "POSTAL_ADDRESS"])
                .setSequencedIdPrimary()
                .create()

        ec.entity
                .makeValue("moqui.PostalAddress")
                .setAll(["contactMechId": contactMech.contactMechId,
                         "toName": toName,
                         "attnName": attnName,
                         "address1": address1,
                         "address2": address2,
                         "city": city,
                         "postalCode": postalCode])
                .create()

        def currDate = new Date()
        ec.entity
                .makeValue("moqui.PartyContactMech")
                .setAll(context + ["partyId" : resultEmail.get("customerList").get(0),
                                   "contactMechId": contactMech.contactMechId,
                                   "contactMechPurposeEnumId" : "PostalPrimary",
                                   "fromDate": currDate])
                .create()
    }
}