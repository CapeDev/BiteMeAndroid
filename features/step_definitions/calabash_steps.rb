require 'calabash-android/calabash_steps'

And /^I search for "([^"]+)"$/ do |search_item|
  page = SearchPage.new(self)
  page.search_yelp(search_item)
end

