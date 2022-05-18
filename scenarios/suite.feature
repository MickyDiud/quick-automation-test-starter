Feature: Catalog Checkout
  """
    @ Test Website:  https://opensea.io/collection/catalog-lu-store

    @ Description:   Automated testing catalog store in OpenSea, which is an simple demo test,
                     and It's only used for test frameworks that are quickly created and executed.

    @ Test Platform: PC
    @ Scenario Steps:

          1) Create a table for fill in all unique items.
          2) Go into each item in the collection.
          3) Click on top right refresh metadata.
          4) Verify that "We queued the item for an update".
          5) Populate status with values
              a)   Clicked = your program has clicked on metadata
              b)   Queued = your program has detected the text “We’ve queued…”
              c)   Error = your program detected some error

    @ Test Data: /data/testdata.xls
    @ Result Report:  ./test-results
    @ Created Date / Modified Date: 10/05/2022
    @ Author / Modified By:  Micky
  """
  @WebTest
  Scenario: Automation test for check metadata for each item

      Given I am go to the home of catalog.lu store on OpenSea Page
      # There is above steps described will be assembled into one implementation.
      Then I verify the status of each value in the collection and populate them

  @TearDown
  Scenario: Tear Down
    And I close browser