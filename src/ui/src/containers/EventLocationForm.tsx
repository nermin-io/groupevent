import React from "react";
import Box from "@/components/Box";
import Flex from "@/components/Flex";
import Label from "@/components/Label";
import Input from "@/components/Input";
import Textarea from "@/components/Textarea";
import Text from "@/components/Text";
import useLocalStorage from "@/hooks/storage";

interface Props {}

const EventLocationForm: React.FC<Props> = () => {
  const { state, setField } = useLocalStorage();

  return (
    <Box>
      <Text css={{ fontSize: 20, fontWeight: 450, marginBottom: 32 }}>
        Where
      </Text>
      <Flex css={{ flexDirection: "column" }}>
        <Box>
          <Label htmlFor="streetAddress">Street Address</Label>
          <Input
            autocomplete="off"
            autocorrect="off"
            autocapitalize="off"
            spellcheck="false"
            placeholder="123 Lonsdale St"
            id="streetAddress"
            value={state.address}
            onChange={(e) => setField("address", e.target.value)}
          />
        </Box>
        <Flex>
          <Box>
            <Label htmlFor="city">City</Label>
            <Input
              autocomplete="off"
              autocorrect="off"
              autocapitalize="off"
              spellcheck="false"
              placeholder="Melbourne"
              id="city"
              value={state.city}
              onChange={(e) => setField("city", e.target.value)}
            />
          </Box>
          <Box>
            <Label htmlFor="state">State</Label>
            <Input
              autocomplete="off"
              autocorrect="off"
              autocapitalize="off"
              spellcheck="false"
              placeholder="VIC"
              id="state"
              value={state.state}
              onChange={(e) => setField("state", e.target.value)}
            />
          </Box>
          <Box>
            <Label htmlFor="postCode">Post Code</Label>
            <Input
              autocomplete="off"
              autocorrect="off"
              autocapitalize="off"
              spellcheck="false"
              placeholder="3000"
              id="postCode"
              value={state.postCode}
              onChange={(e) => setField("postCode", e.target.value)}
            />
          </Box>
        </Flex>
        <Box>
          <Label htmlFor="notes">Notes</Label>
          <Textarea
            placeholder="Parking restrictions, entry details, etc."
            id="notes"
            value={state.notes}
            onChange={(e) => setField("notes", e.target.value)}
          />
        </Box>
      </Flex>
    </Box>
  );
};

export default EventLocationForm;
