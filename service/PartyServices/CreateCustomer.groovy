import org.moqui.context.ExecutionContext
import org.moqui.entity.EntityValue

ExecutionContext ec = context.ec

resultMap = ec.service
        .sync()
        .name("PartyServices.find#Customer")
        .parameters(["emailAddress": emailAddress])
        .call()

if (!resultMap.get("customerList").size()) {
    EntityValue party =  ec.entity
            .makeValue("moqui.Party")
            .setAll(["partyTypeEnumId": "PERSON"])
            .setSequencedIdPrimary()
            .create()

    ec.entity
            .makeValue("moqui.Person")
            .setAll(context + ["partyId" : party.partyId])
            .create()

    ec.entity
            .makeValue("moqui.PartyRole")
            .setAll(["partyId" : party.partyId,
                     "roleTypeEnumId": "CUSTOMER"])
            .create()

    EntityValue contactMech = ec.entity
            .makeValue("moqui.ContactMech")
            .setAll(["infoString": emailAddress])
            .setSequencedIdPrimary()
            .create()

    def currDate = new Date()
    ec.entity
            .makeValue("moqui.PartyContactMech")
            .setAll(context + ["partyId" : party.partyId,
                               "contactMechId": contactMech.contactMechId,
                               "contactMechPurposeEnumId" : "EmailPrimary",
                               "fromDate": currDate])
            .create()
}