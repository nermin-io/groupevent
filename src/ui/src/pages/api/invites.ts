import type { NextApiRequest, NextApiResponse } from "next";
import Groupevent from "@/clients/groupevent";

export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse
) {
  const { attendee, event, data } = req.body;
  const response = await Groupevent.patch(`attendees/${attendee}/invites/${event}`, data);
  return res
    .status(response.status)
    .json(response.data);
}
