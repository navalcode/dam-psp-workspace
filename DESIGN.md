# Design: one request, one template

This workspace contains four independent teaching examples. Three are API/model lessons; their interface is the documented command and observable result. The MVC module has one real page. It is not a portfolio homepage or dashboard.

A standalone generated greeting reference was inspected before implementation. The translation keeps the narrow introductory column, broad open output/form, ruled definition list and quiet footer. The reference's illustrative parameter was corrected to the actual English identifier `name`. An actual seeded RNG selected open output followed by explanation, helpers above fields and focus/border transitions among compatible functional variants. Marketing AIDA sections, stock photos and scroll pinning would obscure this short lesson and were omitted.

## Visual system

Local Outfit/OFL, paper #FAF8F3, ink #202421, muted #5E625C and copper #9B432F follow the shared identity. Desktop uses a 1240px inner width, 36px gap, unequal columns and 48px vertical inset. At720px the layout stacks with 20px gutters; the optional supporting paragraph is omitted, while the task and request explanation remain. The introductory title is two lines at 1366×768 and360×800. A long greeting wraps rather than causing document overflow.

Actions and input are at least 48px high; fields/buttons have 12px corners, visible boundaries and a 3px focus outline. Links use underlining/current-language state. Motion is limited to 160ms field border changes with a reduced-motion override. No JavaScript, external font request, decorative image or motion library is required.

## Behavior

The native GET form puts a fictional name in the URL. The helper explicitly explains this. Empty/whitespace uses localized world. The server rejects more than 80 UTF-16 code units or ISO controls with 400; the draft remains, the field references its error and receives focus. The input maxlength complements server validation. Thymeleaf escapes both text and attribute values.

Spanish is the default UI; English is the base message bundle. System-locale fallback is disabled so a Spanish host cannot override explicitly requested English. Language links preserve the submitted name, including an invalid draft; reset removes it. No session, client storage or account is used. README descriptions and the original Spanish JSON greeting remain separate from page localization.

## Verification and limits

Actual app captures in docs/images show the JAR at 1366×768 and360×800. Browser paths cover keyboard skip, Unicode GET submit, language retention, error focus/translation/reset, and an80-character unbroken name at800×360 without horizontal overflow. The document has zero scripts. Static reduced-motion guards and contrast values were inspected; OS preference emulation, screen readers and formal WCAG certification were not performed.

The four modules have 33 local JUnit tests, packaged-resource checks and real JAR HTTP/model-startup evidence in docs/verification.json. They retain their original dependencies and are historical learning examples, not a security-audited production stack. No new hosted CI is claimed.
