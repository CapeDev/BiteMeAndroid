class SearchPage

  def initialize(calabash)
    @calabash = calabash
  end

  def search_yelp(item)
    @calabash.touch("* marked:'Search'")
    @calabash.performAction('enter_text_into_id_field', item, 'search')
    @calabash.performAction('press_button_with_text', 'GO')
  end
end