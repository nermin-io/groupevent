import React, { useState, useEffect } from "react";
import Box from "../components/Box";
import Flex from "../components/Flex";
import Label from "../components/Label";
import Input from "../components/Input";
import Textarea from "../components/Textarea";
import Text from "../components/Text";
import {WizardComponentProps} from "./Wizard";

interface Props extends WizardComponentProps {}

const EventLocationForm: React.FC<Props> = ({ setIsValid }) => {

  const [streetAddress, setStreetAddress] = useState("");
  const [city, setCity] = useState("");
  const [state, setState] = useState("");
  const [postCode, setPostCode] = useState("");
  const [notes, setNotes] = useState("");

  useEffect(() => {
    if(streetAddress.length === 0 || city.length === 0 || state.length === 0 || postCode.length === 0) {
      setIsValid(false);
    } else {
      setIsValid(true);
    }
  }, [streetAddress, city, state, postCode]);

  return (
    <Box>
      <Text css={{ fontSize: 20, fontWeight: 450, marginBottom: 32 }}>
        Where
      </Text>
      <Flex css={{ flexDirection: "column" }}>
        <Box>
          <Label htmlFor="streetAddress">Street Address</Label>
          <Input placeholder="123 Lonsdale St" id="streetAddress" value={streetAddress} onChange={e => setStreetAddress(e.target.value)} />
        </Box>
        <Flex>
          <Box>
            <Label htmlFor="city">City</Label>
            <Input placeholder="Melbourne" id="city" value={city} onChange={e => setCity(e.target.value)} />
          </Box>
          <Box>
            <Label htmlFor="state">State</Label>
            <Input placeholder="VIC" id="state" value={state} onChange={e => setState(e.target.value)} />
          </Box>
          <Box>
            <Label htmlFor="postCode">Post Code</Label>
            <Input placeholder="3000" id="postCode" value={postCode} onChange={e => setPostCode(e.target.value)} />
          </Box>
        </Flex>
        <Box>
          <Label htmlFor="notes">Notes</Label>
          <Textarea
            placeholder="Parking restrictions, entry details, etc."
            id="notes"
            value={notes}
            onChange={e => setNotes(e.target.value)}
          />
        </Box>
      </Flex>
    </Box>
  );
};

export default EventLocationForm;
