import type { NextApiRequest, NextApiResponse } from "next";
import Groupevent from "@/clients/groupevent";

export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse
) {
  const { organiser, event, token, data } = req.body;
  const response = await Groupevent.patch(`organisers/${organiser}/events/${event}/reschedule?token=${encodeURIComponent(token)}`, data);
  return res
    .status(response.status)
    .json(response.data);
}
