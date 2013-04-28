{
  "businesses": [
    <#list root.getBusinesses() as business>
      {
        "name": "${business.getName()}",
        "image_url": "${business.getImageUrl()}",
        "distance": ${business.getDistance()},
        "rating": ${business.getRating()}
      }<#if business_has_next>,</#if>
    </#list>
  ]
}