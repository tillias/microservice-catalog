---
markmap:
  colorFreezeLevel: 2
---

# Subdomains Classification

## Generic Subdomain

### Characteristics
- Those things that all companies do the ==same way==
- There are known ==best practices== on how to implement such subdomains
- Not a core
  - Does not contain anything special to the organization
  - Still needed for the overall solution to work
### Examples
- Invoicing
- User Identity Management
- API Management
### Strategy
- Purchase from vendor(s)
- Outsorce
- Adopt as open-source projects

## Core Subdomain

### Characteristics
- ==Secret sauce== of your organization
- Things no else does, or does in different ways
- Bring competitive advantage to your company
  - Companies want competitive advantages with high barriers to entry
  - No one will invest in something that their competitors might quickly copy
- Is a foundational concept behind the business
- Should deliver about 20% of the total value of the entire system
- Be about 5% of the code base
- Take about 80% of the effort.
- Business logic changes often... 
  - ...as it’s being optimized over time.

### Examples
- Digital Banking
  - Deposits
  - Lending
  - Personalization
  - Recommendations
  - CIF
### Strategy
- Built in-house
- You want your most experienced people working on it
  - ...instead of having them work complex (and fun) technical or infrastructure problems.
- Implement the most sophisticated business logic modeling patterns here

## Supporting Subdomain

### Characteristics
- Do not offer any competitive edge for the company
  - Thus shouldn’t be complex
  - Business logic should be simple enough
    - Can be rolled out with some rapid application development framework
    - ...or outsourced altogether for offshore development
- There are no existing off-the-shelf solutions
- ==Required== for implementing a ==Core subdomain==
### Examples
 - Digital Banking
   - Document Management System
   - CRM
   - Financial Reporting
   - Notificaitons
### Strategy
- Built in-house
  - Use Automation Frameworks as much as possible
    - Low Code
    - No Code
    - RPA / Workflow Automation Engines
- If complex full of rules, algorithms and invariants?
  - The business people might have gotten over-creative with their requirements
  - ==Simplify==
    - Since this subdomain provides no competitive advantage
    - ...and this complexity is accidental
  - But can’t be simplified?
    - If not reasoning from business
      - ==Still simplify==!
    - It might be a ==Core subdomain in disguise==. 
      - The business might gain an additional competitive edge from this subdomain.
